package com.dream.pay.center.core.refund.fsm;


import com.dream.center.out.mock.dto.ChannelRefundOperationResult;
import com.dream.center.out.mock.dto.OperationStatusEnum;
import com.dream.pay.center.core.refund.enums.RefundTaskEnum;
import com.dream.pay.center.dao.FundsRefundDetailDao;
import com.dream.pay.center.dao.FundsRefundJobDao;
import com.dream.pay.center.model.FundsRefundDetailEntity;
import com.dream.pay.center.model.FundsRefundJobEntity;
import com.dream.pay.center.model.FundsTradeItemsEntity;
import com.dream.pay.center.service.out.ChannelService;
import com.dream.pay.center.status.FundsRefundStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.support.TransactionTemplate;

import javax.annotation.Resource;
import java.util.Date;

/**
 * 提现申请成功==调用渠道退款申请
 *
 * @author 孟振滨
 * @since 2017-11-17
 */
@Slf4j
public class RefundApplySuccessState implements RefundStatusFlow {

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
        if (fundsRefundJobEntity.getJobType() != RefundTaskEnum.INVOKE_CHANNEL_REFUND_APPLY.getCode()) {
            log.info("退款申请成功,状态位不符合执行条件,[{}]!=[{}]", fundsRefundJobEntity.getJobType(), RefundTaskEnum.INVOKE_CHANNEL_REFUND_APPLY.getCode());
            return OperationStatusEnum.UNKNOW;
        }
        return transactionTemplate.execute(status -> {
            ChannelRefundOperationResult channelRefundOperationResult =
                    channelService.refundApply(fundsTradeItemsEntity,fundsRefundDetailEntity);
            if (channelRefundOperationResult.isSuccess()
                    && OperationStatusEnum.FAIL.equals(channelRefundOperationResult.getOperateResultCode())) {
                //更新退款单状态【退款挂起】&失败原因
                fundsRefundDetailEntity.setRefundStatus(FundsRefundStatus.EXCEPTION.getStatus());
                fundsRefundDetailEntity.setChannelReturnNo(channelRefundOperationResult.getRefundChannelNo());
                fundsRefundDetailEntity.setOutErrorCode(channelRefundOperationResult.getErrorCode());
                fundsRefundDetailEntity.setOutErrorMsg(channelRefundOperationResult.getErrorMessage());
                fundsRefundDetailEntity.setUpdateTime(new Date());
                fundsRefundDetailDao.updateByPrimaryKeySelective(fundsRefundDetailEntity);
                //更新任务为人工处理
                fundsRefundJobDao.updateTypeById(RefundTaskEnum.EXCEPTION_SUSPENDED.getCode(), RefundTaskEnum.EXCEPTION_SUSPENDED.getDesc(), fundsRefundJobEntity.getId());

                return OperationStatusEnum.FAIL;
            } else if (channelRefundOperationResult.isSuccess()
                    && OperationStatusEnum.SUCCESS.equals(channelRefundOperationResult.getOperateResultCode())) {
                //更新退款单状态【处理中】
                fundsRefundDetailEntity.setRefundStatus(FundsRefundStatus.PROCESSING.getStatus());
                fundsRefundDetailEntity.setChannelReturnNo(channelRefundOperationResult.getRefundChannelNo());
                fundsRefundDetailEntity.setOutErrorCode(channelRefundOperationResult.getErrorCode());
                fundsRefundDetailEntity.setOutErrorMsg(channelRefundOperationResult.getErrorMessage());
                fundsRefundDetailEntity.setUpdateTime(new Date());
                fundsRefundDetailDao.updateByPrimaryKeySelective(fundsRefundDetailEntity);
                //更新任务为退款查询
                fundsRefundJobDao.updateTypeById(RefundTaskEnum.INVOKE_CHANNEL_REFUND_QUERY.getCode(), RefundTaskEnum.INVOKE_CHANNEL_REFUND_QUERY.getDesc(), fundsRefundJobEntity.getId());

                return OperationStatusEnum.SUCCESS;
            }
            return OperationStatusEnum.UNKNOW;
        });
    }
}
