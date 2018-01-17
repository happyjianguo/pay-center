package com.dream.center.out.mock.facade;

import com.dream.center.out.mock.dto.ChannelRefundOperationResult;
import com.dream.center.out.mock.dto.OperationStatusEnum;
import com.dream.center.out.mock.dto.PayToolRefundApplyRequest;
import com.dream.center.out.mock.dto.PayToolRefundQueryRequest;

import java.util.Date;

/**
 * Created by socket on 2017/11/17.
 */
public class ChannelRefundService {

    public static ChannelRefundOperationResult refundApply(PayToolRefundApplyRequest payToolRefundApplyRequest) {
        ChannelRefundOperationResult channelRefundOperationResult = new ChannelRefundOperationResult();
        channelRefundOperationResult.setSuccess(Boolean.TRUE);
        channelRefundOperationResult.setErrorCode("200");
        channelRefundOperationResult.setErrorMessage("SUCCESS");
        channelRefundOperationResult.setRefundChannelNo("987654321");
        channelRefundOperationResult.setRefundFinishTime(new Date());
        channelRefundOperationResult.setOperateResultCode(OperationStatusEnum.SUCCESS);
        return channelRefundOperationResult;
    }

    public static ChannelRefundOperationResult refundQuery(PayToolRefundQueryRequest payToolRefundQueryRequest) {
        ChannelRefundOperationResult channelRefundOperationResult = new ChannelRefundOperationResult();
        channelRefundOperationResult.setSuccess(Boolean.TRUE);
        channelRefundOperationResult.setErrorCode("200");
        channelRefundOperationResult.setErrorMessage("SUCCESS");
        channelRefundOperationResult.setRefundChannelNo("987654321");
        channelRefundOperationResult.setRefundFinishTime(new Date());
        channelRefundOperationResult.setOperateResultCode(OperationStatusEnum.SUCCESS);
        return channelRefundOperationResult;
    }
}
