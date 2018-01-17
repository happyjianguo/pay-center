package com.dream.pay.center.common.enums;

import lombok.Getter;

/**
 * 支付工具类型枚举
 *
 * @author mengzhenbin
 */
public enum RefundMode {
    ORIGINAL(1, "原路退"),
    BALANCE(2, "余额退"),
    OFFLINE(3, "线下退");

    @Getter
    private int key;
    @Getter
    private String desc;

    RefundMode(int key, String desc) {
        this.desc = desc;
        this.key = key;
    }

    /**
     * 通过枚举<code>code</code>获得枚举
     *
     * @param key
     * @return
     */
    public static RefundMode getByCode(int key) {
        for (RefundMode e : values()) {
            if (e.getKey() == key) {
                return e;
            }
        }
        return null;
    }
}
