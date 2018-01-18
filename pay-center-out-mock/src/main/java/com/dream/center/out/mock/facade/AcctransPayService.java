package com.dream.center.out.mock.facade;

import com.dream.center.out.mock.dto.*;
import com.dream.pay.enums.BizChannelEnum;
import com.dream.pay.enums.PayTool;

import java.util.Date;

/**
 * Created by socket on 2017/11/17.
 */
public class AcctransPayService {

    public static PayOperationResult balancePay(AcctransPayRequest acctransPayRequest) {
        PayOperationResult accountPayOperationResult = new PayOperationResult();
        accountPayOperationResult.setSuccess(Boolean.TRUE);
        accountPayOperationResult.setErrorCode("200");
        accountPayOperationResult.setErrorMessage("SUCCESS");

        accountPayOperationResult.setPayDetailNo(acctransPayRequest.getClientId());
        accountPayOperationResult.setRealAmount(acctransPayRequest.getAmount());
        accountPayOperationResult.setPayVoucherNo("123456789");
        accountPayOperationResult.setAccountingTime(new Date());
        accountPayOperationResult.setPayTool(PayTool.BALANCE);
        accountPayOperationResult.setBizChannel(BizChannelEnum.DREAM);

        accountPayOperationResult.setOperateResultCode(OperationStatusEnum.SUCCESS);
        return accountPayOperationResult;
    }
}
