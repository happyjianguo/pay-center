package com.dream.pay.center.common.enums;

import lombok.Getter;

/**
 * 三级业务标识-业务模式
 * <p>
 * Created by mengzhenbin on 16/7/21.
 */
public enum BizModeEnum {

    TRADING_MODE_GUARANTEE(2001, "担保交易"),

    TRADING_MODE_INSTANT(2002, "即时交易"),

    TRADING_MODE_DELIVERY(2003, "货到付款"),

    AGENT_PAY(2004, "代付"),

    AGENT_RECEIPT(2005,"代收");

    @Getter
    private final Integer code;
    @Getter
    private final String description;

    /**
     * 私有构造函数。
     *
     * @param code        code
     * @param description 描述
     */
    BizModeEnum(Integer code, String description) {
        this.code = code;
        this.description = description;
    }

    /**
     * 通过枚举<code>code</code>获得枚举
     *
     * @param code code
     * @return 类型枚举
     */
    public static BizModeEnum getByCode(Integer code) {
        for (BizModeEnum e : values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }


    /**
     * 通过枚举<code>name</code>获得枚举
     * @param name 名称
     * @return 类型枚举
     */
    public static BizModeEnum select(String name) {
        for (BizModeEnum e : values()) {
            if (e.name().equals(name)) {
                return e;
            }
        }
        return null;
    }

}
