package com.dream.pay.center.core.refund.fsm;

import com.dream.center.out.mock.dto.OperationStatusEnum;
import com.dream.pay.center.core.refund.enums.RefundTaskEnum;
import com.dream.pay.center.dao.FundsRefundDetailDao;
import com.dream.pay.center.dao.FundsRefundJobDao;
import com.dream.pay.center.model.FundsRefundDetailEntity;
import com.dream.pay.center.model.FundsRefundJobEntity;
import com.dream.pay.center.model.FundsTradeItemsEntity;
import com.dream.pay.center.service.out.ChannelService;
import com.dream.pay.center.status.FundsRefundStatus;
import com.dream.pay.channel.access.dto.RefundQueryRepDTO;
import com.dream.pay.channel.access.enums.TradeStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.support.TransactionTemplate;

import javax.annotation.Resource;
import java.util.Date;

/**
 * 退款申请中==调用渠道退款查询
 *
 * @author 孟振滨
 * @since 2017-03-23
 */
@Slf4j
public class RefundProcessingState implements RefundStatusFlow {

    @Resource
    private TransactionTemplate transactionTemplate;

    @Resource
    private ChannelService channelService;

    @Resource
    protected FundsRefundDetailDao fundsRefundDetailDao;

    @Resource
    protected FundsRefundJobDao fundsRefundJobDao;


    @Override
    public OperationStatusEnum statusFlow(FundsTradeItemsEntity fundsTradeItemsEntity, FundsRefundDetailEntity fundsRefundDetailEntity,
                                          FundsRefundJobEntity fundsRefundJobEntity) {
        if (fundsRefundJobEntity.getJobType() != RefundTaskEnum.INVOKE_CHANNEL_REFUND_QUERY.getCode()) {
            log.info("退款处理中,状态位不符合执行条件,[{}]!=[{}]", fundsRefundJobEntity.getJobType(), RefundTaskEnum.INVOKE_CHANNEL_REFUND_QUERY.getCode());
            return OperationStatusEnum.UNKNOW;
        }
        return transactionTemplate.execute(status -> {
            RefundQueryRepDTO refundQueryRepDTO =
                    channelService.refundQuery(fundsRefundDetailEntity);
            if (refundQueryRepDTO.isSuccess()
                    && TradeStatus.FAIL.equals(refundQueryRepDTO.getTradeStatus())) {
                //更新退款单状态【退款挂起】&失败原因
                fundsRefundDetailEntity.setRefundStatus(FundsRefundStatus.EXCEPTION.getStatus());
                fundsRefundDetailEntity.setChannelReturnNo(refundQueryRepDTO.getBankRefundDetailNo());
                fundsRefundDetailEntity.setOutErrorCode(refundQueryRepDTO.getChlRtnCode());
                fundsRefundDetailEntity.setOutErrorMsg(refundQueryRepDTO.getChlRtnMsg());
                fundsRefundDetailEntity.setOutFinishTime(refundQueryRepDTO.getChlFinishTime());
                fundsRefundDetailEntity.setUpdateTime(new Date());
                fundsRefundDetailDao.updateByPrimaryKeySelective(fundsRefundDetailEntity);

                //更新任务为人工处理
                fundsRefundJobDao.updateTypeById(RefundTaskEnum.EXCEPTION_SUSPENDED.getCode(), RefundTaskEnum.EXCEPTION_SUSPENDED.getDesc(), fundsRefundJobEntity.getId());

                //封装返回结果
                return OperationStatusEnum.FAIL;
            } else if (refundQueryRepDTO.isSuccess()
                    && TradeStatus.SUCCESS.equals(refundQueryRepDTO.getTradeStatus())) {
                //更新退款单状态【退款成功】
                fundsRefundDetailEntity.setRefundStatus(FundsRefundStatus.SUCCESS.getStatus());
                fundsRefundDetailEntity.setChannelReturnNo(refundQueryRepDTO.getBankRefundDetailNo());
                fundsRefundDetailEntity.setOutErrorCode(refundQueryRepDTO.getChlRtnCode());
                fundsRefundDetailEntity.setOutErrorMsg(refundQueryRepDTO.getChlRtnMsg());
                fundsRefundDetailEntity.setOutFinishTime(refundQueryRepDTO.getChlFinishTime());
                fundsRefundDetailEntity.setUpdateTime(new Date());
                fundsRefundDetailDao.updateByPrimaryKeySelective(fundsRefundDetailEntity);

                //更新任务为调用账务解冻
                fundsRefundJobDao.updateTypeById(RefundTaskEnum.INVOKE_ACCOUNT_TRANS_IN.getCode(), RefundTaskEnum.INVOKE_ACCOUNT_TRANS_IN.getDesc(), fundsRefundJobEntity.getId());

                //封装返回结果
                return OperationStatusEnum.SUCCESS;
            }
            return OperationStatusEnum.UNKNOW;
        });
    }
}
