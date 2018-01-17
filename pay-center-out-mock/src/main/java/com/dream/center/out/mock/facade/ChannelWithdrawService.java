package com.dream.center.out.mock.facade;

import com.dream.center.out.mock.dto.*;
import com.dream.pay.bean.DataResult;

import java.util.Date;

/**
 * Created by socket on 2017/11/17.
 */
public class ChannelWithdrawService {

    public static ChannelWithdrawOperationResult withdrawApply(PayToolWithdrawApplyRequest payToolWithdrawApplyRequest) {
        ChannelWithdrawOperationResult channelWithdrawOperationResult = new ChannelWithdrawOperationResult();
        channelWithdrawOperationResult.setSuccess(Boolean.TRUE);
        channelWithdrawOperationResult.setErrorCode("200");
        channelWithdrawOperationResult.setErrorMessage("SUCCESS");
        channelWithdrawOperationResult.setWithdrawChannelNo("987654321");
        channelWithdrawOperationResult.setWithdrawFinishTime(new Date());
        channelWithdrawOperationResult.setOperateResultCode(OperationStatusEnum.SUCCESS);
        return channelWithdrawOperationResult;
    }

    public static ChannelWithdrawOperationResult withdrawQuery(PayToolWithdrawQueryRequest payToolWithdrawQueryRequest) {
        ChannelWithdrawOperationResult channelWithdrawOperationResult = new ChannelWithdrawOperationResult();
        channelWithdrawOperationResult.setSuccess(Boolean.TRUE);
        channelWithdrawOperationResult.setErrorCode("200");
        channelWithdrawOperationResult.setErrorMessage("SUCCESS");
        channelWithdrawOperationResult.setWithdrawChannelNo("987654321");
        channelWithdrawOperationResult.setWithdrawFinishTime(new Date());
        channelWithdrawOperationResult.setOperateResultCode(OperationStatusEnum.SUCCESS);
        return channelWithdrawOperationResult;
    }
}
