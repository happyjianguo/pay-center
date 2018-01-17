package com.dream.center.out.mock.enums;

import lombok.Getter;

/**
 * @Author mengzhenbin
 * @Since 2017/12/2
 */
public enum TransCodeEnum {
    PAY(100, "支付"),
    REFUND(200, "退款"),
    RECHARGE(300, "充值"),
    WITHDRAW(400, "提现"),
    TRANSFER(500, "转账"),
    FREEZE(600, "冻结"),
    UNFREEZE(700, "解冻"),;

    @Getter
    private Integer code;
    @Getter
    private String desc;

    TransCodeEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static TransCodeEnum selectByCode(Integer code) {
        for (TransCodeEnum e : values()) {
            if (e.getCode() == code) {
                return e;
            }
        }
        return null;
    }
}
