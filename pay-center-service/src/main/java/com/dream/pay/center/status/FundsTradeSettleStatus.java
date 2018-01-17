package com.dream.pay.center.status;

/**
 * 收单结算状态
 * <p>
 * Created by mengzhenbin on 16/6/24.
 */
public enum FundsTradeSettleStatus {

    NONE(4, "无需结算"),

    CREATE(0, "已创建"),

    SUCCESS(1, "结算成功"),

    FAIL(2, "结算失败");

    private final Integer code;
    private final String description;

    /**
     * 私有构造函数。
     *
     * @param code
     * @param description
     */
    FundsTradeSettleStatus(Integer code, String description) {
        this.code = code;
        this.description = description;
    }

    /**
     * @return Returns the name.
     */
    public Integer getCode() {
        return code;
    }

    /**
     * @return Returns the description.
     */
    public String getDescription() {
        return description;
    }

    /**
     * 通过枚举<code>code</code>获得枚举
     *
     * @param code
     * @return
     */
    public static FundsTradeSettleStatus getByCode(Integer code) {
        for (FundsTradeSettleStatus e : values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }

}
