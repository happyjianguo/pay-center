package com.dream.center.out.mock.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

/**
 * @Author mengzhenbin
 * @Since 2017/12/2
 */
@Setter
@Getter
@ToString(callSuper = true)
public class ChannelRefundOperationResult extends OperationBaseResult {
    /**
     * 退款流水-渠道／第三方
     */
    private String refundChannelNo;

    /**
     * 三方完成时间
     */
    private Date refundFinishTime;

}
