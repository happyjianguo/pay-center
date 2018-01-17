package com.dream.pay.center.api.enums;

import lombok.Getter;

/**
 * 提现返回状态枚举
 *
 * @author mengzhenbin
 * @since 2017-03-20
 */
@Getter
public enum RefundResultEnum {


    REFUND_UN_KNOW(2000, "退款未知状态"),

    REFUND_APPLY_SUCCESS(2010, "退款申请成功"),
    REFUND_APPLY_FAIL(2011, "退款申请失败"),
    REFUND_PROCESSING(2012, "退款处理中"),
    REFUND_SUCCESS(2013, "退款成功"),
    REFUND_FAIL(2014, "退款失败");

    private int code;
    private String desc;

    RefundResultEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}

