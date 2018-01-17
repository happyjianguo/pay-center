package com.dream.pay.center.common.enums;

import lombok.Getter;

import java.util.List;

/**
 * 三级业务标识-产品模式
 * <p>
 * Created by mengzhenbin on 16/7/21.
 */
public enum BizProdEnum {
    TRADING_TYPE_COMMON(1001, "普通交易"),//支持消费，退款
    TRADING_TYPE_BALANCE(1002, "余额"),//支持充值，提现，消费，退款
    TRADING_TYPE_GIFT_CARD(1003, "礼品卡"),//支持充值，提现，消费，退款
    TRADING_TYPE_VALUE_CARD(1004, "储值卡"),//支持充值，提现，消费，退款
    TRADING_TYPE_BOND(1005, "保证金");//支持充值，退款
    @Getter
    private final int code;
    @Getter
    private final String description;

    /**
     * 私有构造函数。
     *
     * @param code        code
     * @param description 描述
     */
    BizProdEnum(Integer code, String description) {
        this.code = code;
        this.description = description;
    }

    /**
     * 通过枚举<code>code</code>获得枚举
     *
     * @param code code
     * @return 类型枚举
     */
    public static BizProdEnum getByCode(int code) {
        for (BizProdEnum e : values()) {
            if (e.getCode() == code) {
                return e;
            }
        }
        return null;
    }

    /**
     * 通过枚举<code>code</code>获得枚举
     *
     * @param name 枚举名称
     * @return 枚举
     */
    public static BizProdEnum select(String name) {
        for (BizProdEnum e : values()) {
            if (e.name().equals(name)) {
                return e;
            }
        }
        return null;
    }


    public boolean in(List<BizProdEnum> bizProd) {
        for (BizProdEnum bizProdEnum : bizProd) {
            if (bizProdEnum.getCode() == this.getCode()) {
                return true;
            }
        }
        return false;
    }
}
