package com.dream.pay.center.core.refund.fsm;

import com.dream.center.out.mock.dto.AccountRefundOperationResult;
import com.dream.center.out.mock.dto.OperationStatusEnum;
import com.dream.pay.bean.Money;
import com.dream.pay.center.core.refund.enums.RefundTaskEnum;
import com.dream.pay.center.core.refund.nsq.RefundNsqMessagePoser;
import com.dream.pay.center.dao.FundsRefundJobDao;
import com.dream.pay.center.dao.FundsTradeInfoDao;
import com.dream.pay.center.model.FundsRefundDetailEntity;
import com.dream.pay.center.model.FundsRefundJobEntity;
import com.dream.pay.center.model.FundsTradeInfoEntity;
import com.dream.pay.center.model.FundsTradeItemsEntity;
import com.dream.pay.center.service.out.SettlementService;
import com.dream.pay.center.status.FundsRefundStatus;
import com.dream.pay.center.status.FundsTradeStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.support.TransactionTemplate;

import javax.annotation.Resource;

/**
 * 退款成功==发送退款成功消息
 *
 * @author 孟振滨
 * @since 2017-03-23
 */
@Slf4j
public class RefundSuccessState extends RefundNsqMessagePoser implements RefundStatusFlow {
    @Resource
    private TransactionTemplate transactionTemplate;
    @Resource
    protected FundsTradeInfoDao fundsTradeInfoDao;
    @Resource
    protected FundsRefundJobDao fundsRefundJobDao;
    @Resource
    private SettlementService settlementService;

    @Override
    public OperationStatusEnum statusFlow(FundsTradeItemsEntity fundsTradeItemsEntity, FundsRefundDetailEntity fundsRefundDetailEntity,
                                          FundsRefundJobEntity fundsRefundJobEntity) {
        if (fundsRefundJobEntity.getJobType() != RefundTaskEnum.INVOKE_ACCOUNT_TRANS_IN.getCode()) {
            log.info("退款成功,状态位不符合执行条件,[{}]!=[{}]", fundsRefundJobEntity.getJobType(), RefundTaskEnum.INVOKE_ACCOUNT_TRANS_IN.getCode());
            return OperationStatusEnum.UNKNOW;
        }
        return transactionTemplate.execute(status -> {
            AccountRefundOperationResult accountOperationResult = settlementService.refundUnFreeze(fundsRefundDetailEntity);
            if (accountOperationResult.isSuccess() &&
                    OperationStatusEnum.SUCCESS.equals(accountOperationResult.getOperateResultCode())) {

                //更新收单退款金额，全额退款后关闭收单
                FundsTradeInfoEntity fundsTradeInfoEntity = fundsTradeInfoDao.selectByPayTradeNoForUpdate(fundsRefundDetailEntity.getPayTradeNo());
                int tradeState = fundsTradeInfoEntity.getTradeState();
                if (isFullRefund(fundsTradeInfoEntity, fundsRefundDetailEntity))
                    tradeState = FundsTradeStatus.CLOSE.getCode();
                fundsTradeInfoDao.updateRefundTradeInfo(tradeState, fundsRefundDetailEntity.getRefundAmount(), fundsRefundDetailEntity.getPayTradeNo());

                //删除任务
                fundsRefundJobDao.deleteByPrimaryKey(fundsRefundJobEntity.getId());

                //发送退款成功消息
                super.sendNsqMessage(fundsRefundDetailEntity, FundsRefundStatus.SUCCESS);
            }
            //封装返回结果
            return OperationStatusEnum.SUCCESS;
        });
    }

    private boolean isFullRefund(FundsTradeInfoEntity fundsTradeInfoEntity, FundsRefundDetailEntity fundsRefundDetailEntity) {
        return new Money(fundsTradeInfoEntity.getRefundAmount())
                .addTo(new Money(fundsRefundDetailEntity.getRefundAmount()))
                .greaterOrEqualThan(new Money(fundsTradeInfoEntity.getTradeAmount()));
    }
}
