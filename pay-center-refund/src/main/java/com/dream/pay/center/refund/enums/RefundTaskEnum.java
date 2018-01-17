package com.dream.pay.center.refund.enums;


import lombok.Getter;

/**
 * 提现任务枚举
 *
 * @author mengzhenbin
 * @since 2017-03-21
 */
public enum RefundTaskEnum {

    INVOKE_ACCOUNT_TRANS_OUT(0, "调用账务冻结"),
    INVOKE_CHANNEL_REFUND_APPLY(1, "调用渠道退款申请"),
    INVOKE_CHANNEL_REFUND_QUERY(2, "调用渠道退款查询"),
    INVOKE_ACCOUNT_TRANS_IN(3, "调用账务解冻"),
    EXCEPTION_SUSPENDED(4, "异常挂起");

    @Getter
    private Integer code;
    @Getter
    private String desc;

    RefundTaskEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

}
