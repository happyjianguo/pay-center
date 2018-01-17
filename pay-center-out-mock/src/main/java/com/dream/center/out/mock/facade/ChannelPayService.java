package com.dream.center.out.mock.facade;

import com.dream.center.out.mock.dto.*;
import org.apache.commons.collections.map.HashedMap;
import org.omg.CORBA.OBJECT_NOT_EXIST;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by socket on 2017/11/17.
 */
public class ChannelPayService {

    public static PayOperationResult payApply(PayToolPayApplyRequest payToolPayApplyRequest) {
        PayOperationResult channelPayOperationResult = new PayOperationResult();
        channelPayOperationResult.setSuccess(Boolean.TRUE);
        channelPayOperationResult.setErrorCode("200");
        channelPayOperationResult.setErrorMessage("SUCCESS");
        channelPayOperationResult.setOperateResultCode(OperationStatusEnum.SUCCESS);
        channelPayOperationResult.setPayChannelNo("987654321");
        channelPayOperationResult.setPayTool(payToolPayApplyRequest.getPayTool());
        channelPayOperationResult.setPaymentTime(new Date());
        channelPayOperationResult.setRealAmount(payToolPayApplyRequest.getTotalAmount());
        channelPayOperationResult.setBizContext(new HashMap());
        Map<String, Object> bodyMap = new HashMap<String, Object>();
        bodyMap.put("key1", "value1");
        bodyMap.put("key2", "value2");
        channelPayOperationResult.setThreePartyReturnValue(bodyMap);
        return channelPayOperationResult;
    }

    public static PayOperationResult payQuery(PayToolPayQueryRequest payToolPayQueryRequest) {
        PayOperationResult channelPayOperationResult = new PayOperationResult();
        channelPayOperationResult.setSuccess(Boolean.TRUE);
        channelPayOperationResult.setErrorCode("200");
        channelPayOperationResult.setErrorMessage("SUCCESS");
        channelPayOperationResult.setOperateResultCode(OperationStatusEnum.SUCCESS);
        channelPayOperationResult.setPayChannelNo("987654321");
        channelPayOperationResult.setPayTool(payToolPayQueryRequest.getPayTool());
        channelPayOperationResult.setPaymentTime(new Date());
        channelPayOperationResult.setRealAmount(1);
        channelPayOperationResult.setBizContext(new HashMap());
        channelPayOperationResult.setThreePartyReturnValue(new HashMap());
        return channelPayOperationResult;
    }
}
