package com.dream.pay.center.common.enums;

import lombok.Getter;

/**
 * 三级业务标识-业务操作类型
 * <p>
 * Created by mengzhenbin on 16/7/21.
 */
public enum BizActionEnum {

    /**
     * 支付
     **/
    PAY(3001, "支付", InOutEnum.IN),
    /**
     * 转账
     **/
    TRANSFER(3002, "转账", InOutEnum.IN),
    /**
     * 退款
     **/
    REFUND(3003, "退款", InOutEnum.OUT),
    /**
     * 提现
     **/
    WITHDRAW(3004, "提现", InOutEnum.OUT),
    /**
     * 充值
     **/
    RECHARGE(3005, "充值", InOutEnum.IN);

    @Getter
    private final int code;
    @Getter
    private final String description;

    @Getter
    private final InOutEnum inOut;

    /**
     * 私有构造函数。
     *
     * @param code        code
     * @param description 描述
     * @param inOut
     */
    BizActionEnum(int code, String description, InOutEnum inOut) {
        this.code = code;
        this.description = description;
        this.inOut = inOut;
    }

    /**
     * 通过枚举<code>code</code>获得枚举
     *
     * @param code code
     * @return 类型枚举
     */
    public static BizActionEnum getByCode(int code) {
        for (BizActionEnum e : values()) {
            if (e.getCode() == code) {
                return e;
            }
        }
        return null;
    }


    /**
     * 通过枚举<code>code</code>获得枚举
     *
     * @param name name
     * @return 类型枚举
     */
    public static BizActionEnum select(String name) {
        for (BizActionEnum e : values()) {
            if (e.name().equals(name)) {
                return e;
            }
        }
        return null;
    }
}
