package com.dream.center.out.mock.facade;

import com.dream.center.out.mock.dto.AccountRefundOperationResult;
import com.dream.center.out.mock.dto.OperationStatusEnum;
import com.dream.center.out.mock.dto.PretreatmentRequest;
import com.dream.center.out.mock.dto.ProcessedRequest;

import java.util.Date;

/**
 * Created by socket on 2017/11/17.
 */
public class SettlementRefundService {

    public static AccountRefundOperationResult refundFreeze(PretreatmentRequest pretreatmentRequest) {
        AccountRefundOperationResult accountRefundOperationResult = new AccountRefundOperationResult();
        accountRefundOperationResult.setSuccess(Boolean.TRUE);
        accountRefundOperationResult.setErrorCode("200");
        accountRefundOperationResult.setErrorMessage("SUCCESS");
        accountRefundOperationResult.setRefundSettleNo("123456789");
        accountRefundOperationResult.setSettlementTime(new Date());
        accountRefundOperationResult.setOperateResultCode(OperationStatusEnum.SUCCESS);
        return accountRefundOperationResult;
    }

    public static AccountRefundOperationResult refundUnFreeze(ProcessedRequest processedRequest) {
        AccountRefundOperationResult accountRefundOperationResult = new AccountRefundOperationResult();
        accountRefundOperationResult.setSuccess(Boolean.TRUE);
        accountRefundOperationResult.setErrorCode("200");
        accountRefundOperationResult.setErrorMessage("SUCCESS");
        accountRefundOperationResult.setRefundSettleNo("123456789");
        accountRefundOperationResult.setSettlementTime(new Date());
        accountRefundOperationResult.setOperateResultCode(OperationStatusEnum.SUCCESS);
        return accountRefundOperationResult;
    }
}
