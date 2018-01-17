package com.dream.pay.center.common.enums;


/**
 * 操作类型枚举
 *
 * @author mengzhenbin
 */
public enum OperatorEnum {

    UNIFIED_ORDER_CREATE("UNIFIED_ORDER_CREATE", "创建统一收单", 3600, 100, "UNIFIED_ORDER_CREATE"),

    UNIFORM_PAYMENT("UNIFORM_PAYMENT", "去付款", 3600, 100, "UNIFORM_PAYMENT"),

    UNIFIED_REFUND_APPLY("UNIFIED_REFUND_APPLY", "退款申请", 3600, 100, "UNIFIED_REFUND_APPLY"),

    UNIFIED_REFUND_QUERY("UNIFIED_REFUND_QUERY", "退款查询", 3600, 100, "UNIFIED_REFUND_QUERY"),

    UNIFIED_WITHDRAW_APPLY("UNIFIED_WITHDRAW_APPLY", "提现申请", 3600, 100, "UNIFIED_WITHDRAW_APPLY"),

    UNIFIED_WITHDRAW_QUERY("UNIFIED_WITHDRAW_QUERY", "提现查询", 3600, 100, "UNIFIED_WITHDRAW_QUERY"),

    UNIFIED_RECHARGE_APPLY("UNIFIED_RECHARGE_APPLY", "充值申请", 3600, 100, "UNIFIED_RECHARGE_APPLY"),;

    private final String code;
    private final String description;
    private final int idenpotencyTimeout;
    private final int bizFlag;
    private final String idenpotencyKeyPreFix;

    /**
     * 私有构造函数。
     *
     * @param code
     * @param description
     * @param idenpotencyTimeout
     * @param bizFlag
     * @param idenpotencyKeyPreFix
     */
    OperatorEnum(String code, String description, int idenpotencyTimeout, int bizFlag, String idenpotencyKeyPreFix) {
        this.code = code;
        this.description = description;
        this.idenpotencyTimeout = idenpotencyTimeout;
        this.bizFlag = bizFlag;
        this.idenpotencyKeyPreFix = idenpotencyKeyPreFix;
    }

    /**
     * @return Returns the name.
     */
    public String getCode() {
        return code;
    }

    /**
     * @return Returns the description.
     */
    public String getDescription() {
        return description;
    }

    public int getIdenpotencyTimeout() {
        return idenpotencyTimeout;
    }

    public String getIdenpotencyKeyPreFix() {
        return idenpotencyKeyPreFix;
    }

    /**
     * 通过枚举<code>code</code>获得枚举
     *
     * @param code
     * @return
     */
    public static OperatorEnum getByCode(String code) {
        for (OperatorEnum e : values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }

    public static boolean isIn(OperatorEnum operatorEnum, OperatorEnum... operatorEnums) {
        for (OperatorEnum oper : operatorEnums) {
            if (oper.equals(operatorEnum)) {
                return true;
            }
        }
        return false;
    }

}
