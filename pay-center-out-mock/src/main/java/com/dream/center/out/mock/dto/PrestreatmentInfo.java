package com.dream.center.out.mock.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

/**
 * @Author mengzhenbin
 * @Since 2018/1/11
 */
@Setter
@Getter
public class PrestreatmentInfo {

    @NotNull(message = "产品码不能为空")
    private String bizProd;

    @NotNull(message = "业务模式码不能为空")
    private String bizModel;

    @NotNull(message = "业务流向码不能为空")
    private String bizAction;

    @NotNull(message = "业务流向子码不能为空")
    private String bizSubAction;

    @NotNull(message = "原外部订单号不能为空")
    private String outBizNo;

    @NotNull(message = "原交易收单号不能为空")
    private String payTradeNo;

    @NotNull(message = "原交易支付流水号不能为空")
    private String payDetailNo;

    @NotNull(message = "退款流水号不能为空")
    private String refundDetailNo;

    @NotNull(message = "退款金额不能为空")
    private long refundAmount;

}
