package com.dream.pay.center.service.out;

import com.dream.center.out.mock.dto.AccountWithdrawOperationResult;
import com.dream.center.out.mock.dto.PayOperationResult;
import com.dream.pay.center.model.FundsPayDetailEntity;
import com.dream.pay.center.model.FundsWithdrawDetailEntity;

/**
 * 调用账务接口
 */
public interface AccountService {
    /**
     * 提现预处理-出账接口
     *
     * @param fundsWithdrawDetailEntity
     * @return OperationResult
     */
    AccountWithdrawOperationResult withdrawTransOut(FundsWithdrawDetailEntity fundsWithdrawDetailEntity);

    /**
     * 提现失败处理-入账接口
     *
     * @param fundsWithdrawDetailEntity
     * @return OperationResult
     */
    AccountWithdrawOperationResult withdrawTransIn(FundsWithdrawDetailEntity fundsWithdrawDetailEntity);

    /**
     * 余额支付
     *
     * @param payDetailEntity
     * @return AccountPayOperationResult
     */
    PayOperationResult payTransOut(FundsPayDetailEntity payDetailEntity);

}
