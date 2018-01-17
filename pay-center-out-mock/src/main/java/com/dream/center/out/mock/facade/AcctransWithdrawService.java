package com.dream.center.out.mock.facade;

import com.dream.center.out.mock.dto.AccountWithdrawOperationResult;
import com.dream.center.out.mock.dto.AcctransWithdrawRequest;
import com.dream.center.out.mock.dto.OperationStatusEnum;

import java.util.Date;

/**
 * Created by socket on 2017/11/17.
 */
public class AcctransWithdrawService {

    public static AccountWithdrawOperationResult withdraw(AcctransWithdrawRequest acctransWithdrawRequest) {
        AccountWithdrawOperationResult accountWithdrawOperationResult = new AccountWithdrawOperationResult();
        accountWithdrawOperationResult.setSuccess(Boolean.TRUE);
        accountWithdrawOperationResult.setErrorCode("200");
        accountWithdrawOperationResult.setErrorMessage("SUCCESS");
        accountWithdrawOperationResult.setWithdrawVoucherNo("123456789");
        accountWithdrawOperationResult.setAccountingTime(new Date());
        accountWithdrawOperationResult.setOperateResultCode(OperationStatusEnum.SUCCESS);
        return accountWithdrawOperationResult;
    }
}
