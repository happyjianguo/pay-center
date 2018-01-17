package com.dream.pay.center.api.enums;

import lombok.Getter;

/**
 * 提现返回状态枚举
 *
 * @author mengzhenbin
 * @since 2017-03-20
 */
@Getter
public enum WithdrawResultEnum {


    WITHDRAW_UN_KNOW(1000,"提现未知状态"),
    WITHDRAW_APPLY_FAIL(1010,"提现未知状态"),
    WITHDRAW_PROCESSING(1011, "提现处理中"),
    WITHDRAW_SUCCESS(1012, "提现成功"),
    WITHDRAW_FAIL(1013, "提现失败"),;

    private int code;
    private String desc;

    WithdrawResultEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}

