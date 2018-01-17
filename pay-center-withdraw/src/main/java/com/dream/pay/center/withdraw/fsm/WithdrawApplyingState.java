package com.dream.pay.center.withdraw.fsm;

import com.dream.center.out.mock.dto.AccountWithdrawOperationResult;
import com.dream.center.out.mock.dto.OperationStatusEnum;
import com.dream.pay.center.dao.FundsTradeInfoDao;
import com.dream.pay.center.dao.FundsWithdrawDetailDao;
import com.dream.pay.center.dao.FundsWithdrawJobDao;
import com.dream.pay.center.model.FundsWithdrawDetailEntity;
import com.dream.pay.center.model.FundsWithdrawJobEntity;
import com.dream.pay.center.service.out.AccountService;
import com.dream.pay.center.status.FundsTradeStatus;
import com.dream.pay.center.status.FundsWithdrawStatus;
import com.dream.pay.center.withdraw.enums.WithdrawTaskEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.support.TransactionTemplate;

import javax.annotation.Resource;
import java.util.Date;

/**
 * 提现申请中==调用账务出账
 *
 * @author 孟振滨
 * @since 2017-11-17
 */
@Slf4j
public class WithdrawApplyingState implements WithdrawStatusFlow {

    @Resource
    private AccountService accountService;

    @Resource
    protected TransactionTemplate transactionTemplate;

    @Resource
    protected FundsTradeInfoDao fundsTradeInfoDao;

    @Resource
    protected FundsWithdrawDetailDao fundsWithdrawDetailDao;

    @Resource
    protected FundsWithdrawJobDao fundsWithdrawJobDao;


    @Override
    public OperationStatusEnum statusFlow(FundsWithdrawDetailEntity fundsWithdrawDetailEntity,
                                          FundsWithdrawJobEntity fundsWithdrawJobEntity) {
        if (fundsWithdrawJobEntity.getJobType() != WithdrawTaskEnum.INVOKE_ACCOUNT_TRANS_OUT.getCode()) {
            log.info("提现预处理,状态位不符合执行条件,[{}]!=[{}]", fundsWithdrawJobEntity.getJobType(), WithdrawTaskEnum.INVOKE_ACCOUNT_TRANS_OUT.getCode());
            return OperationStatusEnum.UNKNOW;
        }
        return transactionTemplate.execute(status -> {
            AccountWithdrawOperationResult accountOperationResult =
                    accountService.withdrawTransOut(fundsWithdrawDetailEntity);

            if (accountOperationResult.isSuccess() &&
                    OperationStatusEnum.FAIL.equals(accountOperationResult.getOperateResultCode())) {
                //冻结失败
                //更新提现单状态【提现申请失败】&失败原因
                fundsWithdrawDetailEntity.setWithdrawStatus(FundsWithdrawStatus.APPLY_FAIL.getStatus());
                fundsWithdrawDetailEntity.setOutReturnNo(accountOperationResult.getWithdrawVoucherNo());
                fundsWithdrawDetailEntity.setOutErrorCode(accountOperationResult.getErrorCode());
                fundsWithdrawDetailEntity.setOutErrorMsg(accountOperationResult.getErrorMessage());
                fundsWithdrawDetailEntity.setOutFinishTime(accountOperationResult.getAccountingTime());
                fundsWithdrawDetailEntity.setUpdateTime(new Date());
                fundsWithdrawDetailDao.updateByPrimaryKeySelective(fundsWithdrawDetailEntity);
                //删除任务
                fundsWithdrawJobDao.deleteByPrimaryKey(fundsWithdrawJobEntity.getId());
                //更新收单状态【交易失败】
                fundsTradeInfoDao.updateStatusByPayTradeNo(FundsTradeStatus.FAILED.getCode(), fundsWithdrawDetailEntity.getPayTradeNo());
                return OperationStatusEnum.FAIL;
            } else if (accountOperationResult.isSuccess() &&
                    OperationStatusEnum.SUCCESS.equals(accountOperationResult.getOperateResultCode())) {
                //冻结成功
                //更新提现单状态【提现申请成功】
                fundsWithdrawDetailEntity.setWithdrawStatus(FundsWithdrawStatus.APPLY_SUCCESS.getStatus());
                fundsWithdrawDetailEntity.setOutReturnNo(accountOperationResult.getWithdrawVoucherNo());
                fundsWithdrawDetailEntity.setOutErrorCode(accountOperationResult.getErrorCode());
                fundsWithdrawDetailEntity.setOutErrorMsg(accountOperationResult.getErrorMessage());
                fundsWithdrawDetailEntity.setUpdateTime(new Date());
                fundsWithdrawDetailDao.updateByPrimaryKeySelective(fundsWithdrawDetailEntity);
                //更新任务类型
                fundsWithdrawJobDao.updateTypeById(WithdrawTaskEnum.INVOKE_CHANNEL_WITHDRAW_APPLY.getCode(), WithdrawTaskEnum.INVOKE_CHANNEL_WITHDRAW_APPLY.getDesc(), fundsWithdrawJobEntity.getId());
                //更新收单状态【交易处理中】
                fundsTradeInfoDao.updateStatusByPayTradeNo(FundsTradeStatus.PROCESSING.getCode(), fundsWithdrawDetailEntity.getPayTradeNo());
                return OperationStatusEnum.SUCCESS;
            }
            return OperationStatusEnum.UNKNOW;
        });
    }
}
