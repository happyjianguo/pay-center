package com.dream.pay.center.core.withdraw.fsm;

import com.dream.center.out.mock.dto.OperationStatusEnum;
import com.dream.pay.center.model.FundsWithdrawDetailEntity;
import com.dream.pay.center.model.FundsWithdrawJobEntity;

/**
 * 提现申请失败==无操作
 *
 * @author 孟振滨
 * @since 2017-11-17
 */
public class WithdrawApplyFailState implements WithdrawStatusFlow {
    @Override
    public OperationStatusEnum statusFlow(FundsWithdrawDetailEntity fundsWithdrawDetailEntity,
                                          FundsWithdrawJobEntity fundsWithdrawJobEntity) {
        return OperationStatusEnum.PROCESSING;
    }
}
