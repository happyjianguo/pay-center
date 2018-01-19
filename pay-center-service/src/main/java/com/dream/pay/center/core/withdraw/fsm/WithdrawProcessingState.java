package com.dream.pay.center.core.withdraw.fsm;

import com.dream.center.out.mock.dto.ChannelWithdrawOperationResult;
import com.dream.center.out.mock.dto.OperationStatusEnum;
import com.dream.pay.center.core.withdraw.enums.WithdrawTaskEnum;
import com.dream.pay.center.dao.FundsWithdrawDetailDao;
import com.dream.pay.center.dao.FundsWithdrawJobDao;
import com.dream.pay.center.model.FundsWithdrawDetailEntity;
import com.dream.pay.center.model.FundsWithdrawJobEntity;
import com.dream.pay.center.service.out.ChannelService;
import com.dream.pay.center.status.FundsWithdrawStatus;
import com.dream.pay.channel.access.dto.WithdrawQueryRepDTO;
import com.dream.pay.channel.access.enums.TradeStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.support.TransactionTemplate;

import javax.annotation.Resource;
import java.util.Date;

/**
 * 提现申请中==调用渠道提现查询
 *
 * @author 孟振滨
 * @since 2017-03-23
 */
@Slf4j
public class WithdrawProcessingState implements WithdrawStatusFlow {

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
        if (fundsWithdrawJobEntity.getJobType() != WithdrawTaskEnum.INVOKE_CHANNEL_WITHDRAW_QUERY.getCode()) {
            log.info("提现处理中,状态位不符合执行条件,[{}]!=[{}]", fundsWithdrawJobEntity.getJobType(), WithdrawTaskEnum.INVOKE_CHANNEL_WITHDRAW_QUERY.getCode());
            return OperationStatusEnum.PROCESSING;
        }
        return transactionTemplate.execute(status -> {
            WithdrawQueryRepDTO withdrawQueryRepDTO =
                    channelService.withdrawQuery(fundsWithdrawDetailEntity);
            if (withdrawQueryRepDTO.isSuccess()
                    && TradeStatus.FAIL.equals(withdrawQueryRepDTO.getTradeStatus())) {
                //更新提现单状态【提现挂起】&失败原因
                fundsWithdrawDetailEntity.setWithdrawStatus(FundsWithdrawStatus.EXCEPTION.getStatus());
                fundsWithdrawDetailEntity.setOutReturnNo(withdrawQueryRepDTO.getWithdrawChannelNo());
                fundsWithdrawDetailEntity.setOutErrorCode(withdrawQueryRepDTO.getChlRtnCode());
                fundsWithdrawDetailEntity.setOutErrorMsg(withdrawQueryRepDTO.getChlRtnMsg());
                fundsWithdrawDetailEntity.setOutFinishTime(withdrawQueryRepDTO.getChlFinishTime());
                fundsWithdrawDetailEntity.setUpdateTime(new Date());
                fundsWithdrawDetailDao.updateByPrimaryKeySelective(fundsWithdrawDetailEntity);

                //更新任务为人工处理
                fundsWithdrawJobDao.updateTypeById(WithdrawTaskEnum.EXCEPTION_SUSPENDED.getCode(), WithdrawTaskEnum.EXCEPTION_SUSPENDED.getDesc(), fundsWithdrawJobEntity.getId());

                //封装返回结果
                return OperationStatusEnum.FAIL;
            } else if (withdrawQueryRepDTO.isSuccess()
                    && TradeStatus.SUCCESS.equals(withdrawQueryRepDTO.getTradeStatus())) {
                //更新提现单状态【提现成功】
                fundsWithdrawDetailEntity.setWithdrawStatus(FundsWithdrawStatus.SUCCESS.getStatus());
                fundsWithdrawDetailEntity.setOutReturnNo(withdrawQueryRepDTO.getWithdrawChannelNo());
                fundsWithdrawDetailEntity.setOutErrorCode(withdrawQueryRepDTO.getChlRtnCode());
                fundsWithdrawDetailEntity.setOutErrorMsg(withdrawQueryRepDTO.getChlRtnMsg());
                fundsWithdrawDetailEntity.setOutFinishTime(withdrawQueryRepDTO.getChlFinishTime());
                fundsWithdrawDetailEntity.setUpdateTime(new Date());
                fundsWithdrawDetailDao.updateByPrimaryKeySelective(fundsWithdrawDetailEntity);

                //更新任务为调用账务解冻
                fundsWithdrawJobDao.updateTypeById(WithdrawTaskEnum.INVOKE_ACCOUNT_TRANS_IN.getCode(), WithdrawTaskEnum.INVOKE_ACCOUNT_TRANS_IN.getDesc(), fundsWithdrawJobEntity.getId());

                //封装返回结果
                return OperationStatusEnum.SUCCESS;
            }
            return OperationStatusEnum.UNKNOW;
        });
    }
}
