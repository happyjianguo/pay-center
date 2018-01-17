package com.dream.center.out.mock.facade;

import com.dream.center.out.mock.dto.AcctransPayRequest;
import com.dream.center.out.mock.dto.OperationStatusEnum;
import com.dream.center.out.mock.dto.PayOperationResult;
import com.dream.pay.center.common.enums.ChannelType;
import com.dream.pay.center.common.enums.PayTool;

import java.util.Date;

/**
 * Created by socket on 2017/11/17.
 */
public class ActivePayService {

    public static PayOperationResult activePay(AcctransPayRequest acctransPayRequest) {
        PayOperationResult activePayOperationResult = new PayOperationResult();
        activePayOperationResult.setSuccess(Boolean.TRUE);
        activePayOperationResult.setErrorCode("200");
        activePayOperationResult.setErrorMessage("SUCCESS");

        activePayOperationResult.setPayDetailNo(acctransPayRequest.getClientId());
        activePayOperationResult.setRealAmount(acctransPayRequest.getAmount());
        activePayOperationResult.setPayVoucherNo("232232789899090");
        activePayOperationResult.setAccountingTime(new Date());
        activePayOperationResult.setPayTool(PayTool.LI_JIAN);
        activePayOperationResult.setBizChannel(ChannelType.DREAM);

        activePayOperationResult.setOperateResultCode(OperationStatusEnum.SUCCESS);
        return activePayOperationResult;
    }
}
