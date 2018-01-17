package com.dream.center.out.mock.enums;

import lombok.Getter;

/**
 * @Author mengzhenbin
 * @Since 2017/12/2
 */
public enum TransSubCodeEnum {

    WITHDRAW_PRE_WITHDRAW(400, "预提现", TransCodeEnum.WITHDRAW),
    WITHDRAW_SUCCESS(401, "提现成功", TransCodeEnum.WITHDRAW),
    WITHDRAW_FAIL(402, "提现失败", TransCodeEnum.WITHDRAW),

    PAY_INNERPAYTOOL_PAY(100, "内部支付工具支付", TransCodeEnum.PAY),;

    @Getter
    private TransCodeEnum transCodeEnum;
    @Getter
    private Integer code;
    @Getter
    private String desc;

    TransSubCodeEnum(Integer code, String desc, TransCodeEnum transCodeEnum) {
        this.transCodeEnum = transCodeEnum;
        this.code = code;
        this.desc = desc;
    }

    public static TransSubCodeEnum selectByCode(Integer code) {
        for (TransSubCodeEnum e : values()) {
            if (e.getCode() == code) {
                return e;
            }
        }
        return null;
    }
}
