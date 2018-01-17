package com.dream.pay.center.common.enums;

/**
 * 支付工具类型枚举
 *
 * @author mengzhenbin
 */
public enum PayMode {
    QUICK_PAY_1(1, "快捷支付", PayToolType.QUICK_PAY),
    QUICK_PAY_9(9, "快捷+内部工具支付", PayToolType.QUICK_PAY),
    QUICK_PAY_17(17, "快捷+活动支付", PayToolType.QUICK_PAY),
    QUICK_PAY_25(25, "快捷+内部工具+活动支付", PayToolType.QUICK_PAY),

    PLAT_PAY_2(2, "三方支付", PayToolType.PLAT_PAY),
    PLAT_PAY_10(10, "三方+内部工具支付", PayToolType.PLAT_PAY),
    PLAT_PAY_18(18, "三方+活动支付", PayToolType.PLAT_PAY),
    PLAT_PAY_26(26, "三方+内部工具+活动支付", PayToolType.PLAT_PAY),


    BANK_PAY_4(4, "银行支付", PayToolType.BANK_PAY),
    BANK_PAY_12(12, "银行+内部工具支付", PayToolType.BANK_PAY),
    BANK_PAY_20(20, "银行+活动支付", PayToolType.BANK_PAY),
    BANK_PAY_28(28, "银行+内部工具+活动支付", PayToolType.BANK_PAY),

    INNER_PAY_8(8, "内部工具支付", PayToolType.INNER_PAY),
    INNER_PAY_24(24, "内部工具+活动支付", PayToolType.INNER_PAY);


    private final int key;
    private final String desc;
    private final PayToolType mainType;

    PayMode(int key, String desc, PayToolType mainType) {
        this.desc = desc;
        this.mainType = mainType;
        this.key = key;
    }

    public int key() {
        return key;
    }

    public String desc() {
        return desc;
    }

    public PayToolType mainType() {
        return mainType;
    }

    public static PayToolType select(int value) {
        for (PayMode payMode : PayMode.values()) {
            if (payMode.key == value) {
                return payMode.mainType;
            }
        }
        return null;
    }

    /**
     * 通过枚举<code>code</code>获得枚举
     *
     * @param key
     * @return
     */
    public static PayMode getByCode(int key) {
        for (PayMode e : values()) {
            if (e.key() == key) {
                return e;
            }
        }
        return null;
    }
}
