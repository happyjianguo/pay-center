package com.dream.pay.center.withdraw.fsm;

import com.dream.center.out.mock.dto.OperationStatusEnum;
import com.dream.pay.center.model.FundsWithdrawDetailEntity;
import com.dream.pay.center.model.FundsWithdrawJobEntity;

/**
 * 提现状态机流转接口
 *
 * @author 孟振滨
 * @since 2017-12-02
 */
public interface WithdrawStatusFlow {
    OperationStatusEnum statusFlow(FundsWithdrawDetailEntity fundsWithdrawDetailEntity,
                                   FundsWithdrawJobEntity fundsWithdrawJobEntity);
}
