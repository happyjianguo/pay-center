package com.dream.pay.center.core.refund.fsm;

import com.dream.center.out.mock.dto.AccountRefundOperationResult;
import com.dream.center.out.mock.dto.OperationStatusEnum;
import com.dream.pay.center.core.refund.enums.RefundTaskEnum;
import com.dream.pay.center.dao.FundsRefundDetailDao;
import com.dream.pay.center.dao.FundsRefundJobDao;
import com.dream.pay.center.dao.FundsTradeInfoDao;
import com.dream.pay.center.dao.FundsTradeItemsDao;
import com.dream.pay.center.model.FundsRefundDetailEntity;
import com.dream.pay.center.model.FundsRefundJobEntity;
import com.dream.pay.center.model.FundsTradeItemsEntity;
import com.dream.pay.center.service.out.SettlementService;
import com.dream.pay.center.status.FundsRefundStatus;
import com.dream.pay.center.status.FundsTradeItemStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.support.TransactionTemplate;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * 退款申请中==调用清结算冻结商户余额
 *
 * @author 孟振滨
 * @since 2017-11-17
 */
@Slf4j
public class RefundApplyingState implements RefundStatusFlow {

    @Resource
    private SettlementService settlementService;

    @Resource
    protected TransactionTemplate transactionTemplate;

    @Resource
    protected FundsTradeInfoDao fundsTradeInfoDao;

    @Resource
    protected FundsTradeItemsDao fundsTradeItemsDaoDao;

    @Resource
    protected FundsRefundDetailDao fundsRefundDetailDao;

    @Resource
    protected FundsRefundJobDao fundsRefundJobDao;

    @Override
    public OperationStatusEnum statusFlow(FundsTradeItemsEntity fundsTradeItemsEntity, FundsRefundDetailEntity fundsRefundDetailEntity,
                                          FundsRefundJobEntity fundsRefundJobEntity) {
        if (null != fundsRefundJobEntity && fundsRefundJobEntity.getJobType() != RefundTaskEnum.INVOKE_ACCOUNT_TRANS_OUT.getCode()) {
            log.info("退款预处理,状态位不符合执行条件,[{}]!=[{}]", fundsRefundJobEntity.getJobType(), RefundTaskEnum.INVOKE_ACCOUNT_TRANS_OUT.getCode());
            return OperationStatusEnum.UNKNOW;
        }

        List<FundsRefundDetailEntity> refundDetailEntities = fundsRefundDetailDao.loadByPayItemsNo(fundsTradeItemsEntity.getPayTradeItemsNo());
        List<FundsRefundJobEntity> refundJobEntities = fundsRefundJobDao.loadByPayItemsNo(fundsTradeItemsEntity.getPayTradeItemsNo());

        if (null == refundDetailEntities || null == refundJobEntities) {
            log.error("退款预处理,未找到相应的退款明细单或退款任务{}-{}", refundDetailEntities, refundJobEntities);
            return OperationStatusEnum.FAIL;
        }
        return transactionTemplate.execute(status -> {
            AccountRefundOperationResult accountOperationResult =
                    settlementService.refundFreeze(fundsTradeItemsEntity, refundDetailEntities);

            if (accountOperationResult.isSuccess() &&
                    OperationStatusEnum.FAIL.equals(accountOperationResult.getOperateResultCode())) {
                //冻结失败
                //更新退款单状态【退款申请失败】&失败原因
                for (FundsRefundDetailEntity refundDetail : refundDetailEntities) {
                    refundDetail.setRefundStatus(FundsRefundStatus.APPLY_FAIL.getStatus());
                    refundDetail.setChannelReturnNo(accountOperationResult.getRefundSettleNo());
                    refundDetail.setOutErrorCode(accountOperationResult.getErrorCode());
                    refundDetail.setOutErrorMsg(accountOperationResult.getErrorMessage());
                    refundDetail.setOutFinishTime(accountOperationResult.getSettlementTime());
                    refundDetail.setUpdateTime(new Date());
                    fundsRefundDetailDao.updateByPrimaryKeySelective(refundDetail);
                }
                //删除任务
                for (FundsRefundJobEntity refundJob : refundJobEntities) {
                    fundsRefundJobDao.deleteByPrimaryKey(refundJob.getId());
                }

                //减少退款中金额字段
                fundsTradeInfoDao.decRefundInProcessAmount(fundsRefundDetailEntity.getPayTradeNo(), fundsRefundDetailEntity.getRefundAmount());

                fundsTradeItemsEntity.setTradeState(FundsTradeItemStatus.FAILED.getCode());
                fundsTradeItemsDaoDao.updateByPrimaryKeySelective(fundsTradeItemsEntity);
                //发送申请退款失败消息 TODO

                return OperationStatusEnum.FAIL;
            } else if (accountOperationResult.isSuccess() &&
                    OperationStatusEnum.SUCCESS.equals(accountOperationResult.getOperateResultCode())) {
                //冻结成功
                //更新退款单状态【退款申请成功】
                for (FundsRefundDetailEntity refundDetail : refundDetailEntities) {
                    refundDetail.setRefundStatus(FundsRefundStatus.APPLY_SUCCESS.getStatus());
                    refundDetail.setChannelReturnNo(accountOperationResult.getRefundSettleNo());
                    refundDetail.setOutErrorCode(accountOperationResult.getErrorCode());
                    refundDetail.setOutErrorMsg(accountOperationResult.getErrorMessage());
                    refundDetail.setUpdateTime(new Date());
                    fundsRefundDetailDao.updateByPrimaryKeySelective(refundDetail);
                }
                //更新任务类型
                for (FundsRefundJobEntity refundJob : refundJobEntities) {
                    fundsRefundJobDao.updateTypeById(RefundTaskEnum.INVOKE_CHANNEL_REFUND_APPLY.getCode(), RefundTaskEnum.INVOKE_CHANNEL_REFUND_APPLY.getDesc(), refundJob.getId());
                }
                return OperationStatusEnum.SUCCESS;
            }
            return OperationStatusEnum.UNKNOW;
        });
    }
}
