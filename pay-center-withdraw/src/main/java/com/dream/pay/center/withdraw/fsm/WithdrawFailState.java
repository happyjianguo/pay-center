package com.dream.pay.center.withdraw.fsm;

import com.dream.center.out.mock.dto.AccountWithdrawOperationResult;
import com.dream.center.out.mock.dto.OperationStatusEnum;
import com.dream.pay.center.api.message.NsqEntity;
import com.dream.pay.center.api.message.WithdrawNsqMessage;
import com.dream.pay.center.common.constants.BizCodeUtils;
import com.dream.pay.center.common.constants.UnifiedBizCode;
import com.dream.pay.center.component.NsqMessagePoser;
import com.dream.pay.center.dao.FundsTradeInfoDao;
import com.dream.pay.center.dao.FundsWithdrawJobDao;
import com.dream.pay.center.model.FundsWithdrawDetailEntity;
import com.dream.pay.center.model.FundsWithdrawJobEntity;
import com.dream.pay.center.service.out.AccountService;
import com.dream.pay.center.status.FundsTradeStatus;
import com.dream.pay.center.status.FundsWithdrawStatus;
import com.dream.pay.center.withdraw.enums.WithdrawTaskEnum;
import com.dream.pay.utils.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.support.TransactionTemplate;

import javax.annotation.Resource;

/**
 * 提现失败==调用账务入账
 *
 * @author 孟振滨
 * @since 2017-10-23
 */
@Slf4j
public class WithdrawFailState extends WithdrawNsqMessagePoser implements WithdrawStatusFlow {
    @Resource
    private AccountService accountService;
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
        if (fundsWithdrawJobEntity.getJobType() != WithdrawTaskEnum.WITHDRAW_FAIL.getCode()) {
            log.info("提现失败,状态位不符合执行条件,[{}]!=[{}]", fundsWithdrawJobEntity.getJobType(), WithdrawTaskEnum.WITHDRAW_FAIL.getCode());
            return OperationStatusEnum.PROCESSING;
        }
        return transactionTemplate.execute(status -> {
            AccountWithdrawOperationResult accountOperationResult =
                    accountService.withdrawTransIn(fundsWithdrawDetailEntity);
            if (accountOperationResult.isSuccess() &&
                    OperationStatusEnum.SUCCESS.equals(accountOperationResult.getOperateResultCode())) {
                //更新收单状态【交易失败】
                fundsTradeInfoDao.updateStatusByPayTradeNo(FundsTradeStatus.FAILED.getCode(), fundsWithdrawDetailEntity.getPayTradeNo());

                //删除任务
                fundsWithdrawJobDao.deleteByPrimaryKey(fundsWithdrawJobEntity.getId());

                //发送提现失败消息
                super.sendNsqMessage(fundsWithdrawDetailEntity, FundsWithdrawStatus.FAIL);

                //封装返回结果
                return OperationStatusEnum.SUCCESS;
            }
            return OperationStatusEnum.UNKNOW;
        });
    }
}
