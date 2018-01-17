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
public class AccountRefundOperationResult extends OperationBaseResult {
    /**
     * 退款清结算处理凭证单号
     */
    private String refundSettleNo;

    /**
     * 退款清结算处理时间
     */
    private Date settlementTime;

}
