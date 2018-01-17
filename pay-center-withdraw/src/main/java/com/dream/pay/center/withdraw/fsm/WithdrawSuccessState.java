package com.dream.pay.center.withdraw.fsm;

import com.dream.center.out.mock.dto.OperationStatusEnum;
import com.dream.pay.center.dao.FundsTradeInfoDao;
import com.dream.pay.center.dao.FundsWithdrawJobDao;
import com.dream.pay.center.model.FundsWithdrawDetailEntity;
import com.dream.pay.center.model.FundsWithdrawJobEntity;
import com.dream.pay.center.status.FundsTradeStatus;
import com.dream.pay.center.status.FundsWithdrawStatus;
import com.dream.pay.center.withdraw.enums.WithdrawTaskEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.support.TransactionTemplate;

import javax.annotation.Resource;

/**
 * 提现成功==发送提现成功消息
 *
 * @author 孟振滨
 * @since 2017-03-23
 */
@Slf4j
public class WithdrawSuccessState extends WithdrawNsqMessagePoser implements WithdrawStatusFlow {
    @Resource
    private TransactionTemplate transactionTemplate;
    @Resource
    protected FundsTradeInfoDao fundsTradeInfoDao;
    @Resource
    protected FundsWithdrawJobDao fundsWithdrawJobDao;

    @Value("${withdraw_messgae_topic}")
    private String topic;


    @Override
    public OperationStatusEnum statusFlow(FundsWithdrawDetailEntity fundsWithdrawDetailEntity,
                                          FundsWithdrawJobEntity fundsWithdrawJobEntity) {
        if (fundsWithdrawJobEntity.getJobType() != WithdrawTaskEnum.INVOKE_ACCOUNT_TRANS_IN.getCode()) {
            log.info("提现成功,状态位不符合执行条件,[{}]!=[{}]", fundsWithdrawJobEntity.getJobType(), WithdrawTaskEnum.INVOKE_ACCOUNT_TRANS_IN.getCode());
            return OperationStatusEnum.PROCESSING;
        }
        return transactionTemplate.execute(status -> {
            //更新收单状态【交易成功】
            fundsTradeInfoDao.updateStatusByPayTradeNo(FundsTradeStatus.SUCCEED.getCode(), fundsWithdrawDetailEntity.getPayTradeNo());

            //删除任务
            fundsWithdrawJobDao.deleteByPrimaryKey(fundsWithdrawJobEntity.getId());

            //发送提现成功消息
            super.sendNsqMessage(fundsWithdrawDetailEntity, FundsWithdrawStatus.SUCCESS);
            //封装返回结果
            return OperationStatusEnum.SUCCESS;
        });
    }


}
