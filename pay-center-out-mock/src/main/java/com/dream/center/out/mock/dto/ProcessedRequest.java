package com.dream.center.out.mock.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

/**
 * 清结算退款解冻操作参数
 *
 * @Author mengzhenbin
 * @Since 2017/12/13
 */
@Setter
@Getter
public class ProcessedRequest {
    @NotNull(message = "原交易外部订单号不能为空")
    private String outBizNo;

    @NotNull(message = "原交易收单号不能为空")
    private String acquireNo;

    @NotNull(message = "原交易支付流水号不能为空")
    private String payDetailNo;

    @NotNull(message = "退款流水号不能为空")
    private String refundDetailNo;
}
