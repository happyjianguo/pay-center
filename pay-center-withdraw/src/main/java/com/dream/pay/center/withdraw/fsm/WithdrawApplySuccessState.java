package com.dream.pay.center.withdraw.fsm;


import com.dream.center.out.mock.dto.ChannelWithdrawOperationResult;
import com.dream.center.out.mock.dto.OperationStatusEnum;
import com.dream.pay.center.dao.FundsWithdrawDetailDao;
import com.dream.pay.center.dao.FundsWithdrawJobDao;
import com.dream.pay.center.model.FundsWithdrawDetailEntity;
import com.dream.pay.center.model.FundsWithdrawJobEntity;
import com.dream.pay.center.service.out.ChannelService;
import com.dream.pay.center.status.FundsWithdrawStatus;
import com.dream.pay.center.withdraw.enums.WithdrawTaskEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.support.TransactionTemplate;

import javax.annotation.Resource;
import java.util.Date;

/**
 * 提现申请成功==调用渠道提现申请
 *
 * @author 孟振滨
 * @since 2017-11-17
 */
@Slf4j
public class WithdrawApplySuccessState implements WithdrawStatusFlow {

    @Resource
    private TransactionTemplate transactionTemplate;

    @Resource
    private ChannelService channelService;

    @Resource
    protected FundsWithdrawDetailDao fundsWithdrawDetailDao;

    @Resource
    protected FundsWithdrawJobDao fundsWithdrawJobDao;

    @Override
    public OperationStatusEnum statusFlow(FundsWithdrawDetailEntity fundsWithdrawDetailEntity,
                                          FundsWithdrawJobEntity fundsWithdrawJobEntity) {
        if (fundsWithdrawJobEntity.getJobType() != WithdrawTaskEnum.INVOKE_CHANNEL_WITHDRAW_APPLY.getCode()) {
            log.info("提现申请成功,状态位不符合执行条件,[{}]!=[{}]", fundsWithdrawJobEntity.getJobType(), WithdrawTaskEnum.INVOKE_CHANNEL_WITHDRAW_APPLY.getCode());
            return OperationStatusEnum.UNKNOW;
        }
        return transactionTemplate.execute(status -> {
            ChannelWithdrawOperationResult channelWithdrawOperationResult =
                    channelService.withdrawApply(fundsWithdrawDetailEntity);
            if (channelWithdrawOperationResult.isSuccess()
                    && OperationStatusEnum.FAIL.equals(channelWithdrawOperationResult.getOperateResultCode())) {
                //更新提现单状态【提现挂起】&失败原因
                fundsWithdrawDetailEntity.setWithdrawStatus(FundsWithdrawStatus.EXCEPTION.getStatus());
                fundsWithdrawDetailEntity.setOutReturnNo(channelWithdrawOperationResult.getWithdrawChannelNo());
                fundsWithdrawDetailEntity.setOutErrorCode(channelWithdrawOperationResult.getErrorCode());
                fundsWithdrawDetailEntity.setOutErrorMsg(channelWithdrawOperationResult.getErrorMessage());
                fundsWithdrawDetailEntity.setUpdateTime(new Date());
                fundsWithdrawDetailDao.updateByPrimaryKeySelective(fundsWithdrawDetailEntity);
                //更新任务为人工处理
                fundsWithdrawJobDao.updateTypeById(WithdrawTaskEnum.EXCEPTION_SUSPENDED.getCode(), WithdrawTaskEnum.EXCEPTION_SUSPENDED.getDesc(), fundsWithdrawJobEntity.getId());

                return OperationStatusEnum.FAIL;
            } else if (channelWithdrawOperationResult.isSuccess()
                    && OperationStatusEnum.SUCCESS.equals(channelWithdrawOperationResult.getOperateResultCode())
                    || OperationStatusEnum.PROCESSING.equals(channelWithdrawOperationResult.getOperateResultCode())
                    || OperationStatusEnum.UNKNOW.equals(channelWithdrawOperationResult.getOperateResultCode())) {
                //更新提现单状态【提现处理中】&失败原因
                fundsWithdrawDetailEntity.setWithdrawStatus(FundsWithdrawStatus.PROCESSING.getStatus());
                fundsWithdrawDetailEntity.setOutReturnNo(channelWithdrawOperationResult.getWithdrawChannelNo());
                fundsWithdrawDetailEntity.setOutErrorCode(channelWithdrawOperationResult.getErrorCode());
                fundsWithdrawDetailEntity.setOutErrorMsg(channelWithdrawOperationResult.getErrorMessage());
                fundsWithdrawDetailEntity.setUpdateTime(new Date());
                fundsWithdrawDetailDao.updateByPrimaryKeySelective(fundsWithdrawDetailEntity);
                //更新任务为提现查询
                fundsWithdrawJobDao.updateTypeById(WithdrawTaskEnum.INVOKE_CHANNEL_WITHDRAW_QUERY.getCode(), WithdrawTaskEnum.INVOKE_CHANNEL_WITHDRAW_QUERY.getDesc(), fundsWithdrawJobEntity.getId());

                return OperationStatusEnum.SUCCESS;
            }
            return OperationStatusEnum.UNKNOW;
        });
    }
}
