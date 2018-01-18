package com.dream.pay.center.status;

import lombok.Getter;

/**
 * 提现状态
 */
public enum FundsJobStatus {
    /**
     * 待执行
     */
    TODO(0, "待执行"),
    /**
     * 执行中
     */
    DOING(1, "执行中"),
    /**
     * 执行成功
     */
    DONE_SUCCESS(2, "执行成功"),
    /**
     * 执行失败
     */
    DONE_FAIL(3, "执行失败"),;

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
    FundsJobStatus(Integer code, String description) {
        this.code = code;
        this.description = description;
    }

    /**
     * 通过枚举<code>code</code>获得枚举
     *
     * @param code code
     * @return 支付收单状态机
     */
    public static FundsJobStatus getByCode(Integer code) {
        for (FundsJobStatus e : values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }
}

