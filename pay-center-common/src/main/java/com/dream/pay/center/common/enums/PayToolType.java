package com.dream.pay.center.common.enums;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 支付工具类型枚举
 *
 * @author mengzhenbin
 */
public enum PayToolType {
    /**
     * 支付工具类型
     */
    QUICK_PAY((byte) 1, "QP", "快捷支付"),
    PLAT_PAY((byte) 2, "GW", "平台支付"),
    BANK_PAY((byte) 4, "GW", "网银支付"),
    INNER_PAY((byte) 8, "IN", "内部支付"),
    ACTIVE_PAY((byte) 16, "AC", "活动支付");


    private final byte key;
    private final String chlCode;
    private final String value;

    PayToolType(byte key, String chlCode, String value) {
        this.value = value;
        this.chlCode = chlCode;
        this.key = key;
    }

    public int key() {
        return key;
    }

    public String value() {
        return value;
    }

    public String chlCode() {
        return chlCode;
    }

    public static PayToolType toPayToolType(int value) {
        for (PayToolType payToolType : PayToolType.values()) {
            if (payToolType.key == value) {
                return valueOf(payToolType.name());
            }
        }
        return null;
    }

    public static PayToolType toPayToolType(String name) {
        if (StringUtils.isBlank(name)) {
            return null;
        }

        for (PayToolType payToolType : PayToolType.values()) {
            if (StringUtils.equals(payToolType.name().toLowerCase(), name.toLowerCase())) {
                return valueOf(payToolType.name());
            }
        }
        return null;
    }

    public static List<PayToolType> toPayToolArray(Integer payMode) {
        List<PayToolType> payTools = new ArrayList<PayToolType>();
        for (PayToolType payToolType : PayToolType.values()) {
            if ((payMode & payToolType.key()) > 0) {
                payTools.add(payToolType);
            }
        }
        return payTools;
    }
}
