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
public class ChannelWithdrawOperationResult extends OperationBaseResult {
    /**
     * 提现流水-渠道／第三方
     */
    private String withdrawChannelNo;

    /**
     * 三方完成时间
     */
    private Date withdrawFinishTime;

}
