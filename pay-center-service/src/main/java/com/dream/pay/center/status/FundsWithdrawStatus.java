package com.dream.pay.center.status;

import lombok.Getter;

/**
 * 提现状态枚举
 * Created by mengzhenbin on 16/6/24.
 */
public enum FundsWithdrawStatus {

    /**
     * 申请中
     */
    APPLYING(0, "申请中"),
    /**
     * 申请成功
     */
    APPLY_SUCCESS(1, "申请成功"),
    /**
     * 申请失败
     */
    APPLY_FAIL(2, "申请失败"),
    /**
     * 提现处理中
     */
    PROCESSING(3, "提现处理中"),
    /**
     * 风控拦截
     */
    RISK_FAIL(4, "风控拦截"),
    /**
     * 提现异常转人工处理
     */
    EXCEPTION(5, "提现异常转人工处理"),
    /**
     * 提现失败
     */
    FAIL(6, "提现失败"),
    /**
     * 提现成功
     */
    SUCCESS(7, "提现成功");


    @Getter
    private int status;

    private String desc;


    FundsWithdrawStatus(int status, String desc) {
        this.status = status;
        this.desc = desc;
    }


    public static FundsWithdrawStatus getByCode(Integer state) {
        if (null == state) {
            return null;
        }
        for (FundsWithdrawStatus refundStatus : values()) {
            if (state.equals(refundStatus.getStatus())) {
                return refundStatus;
            }
        }
        return null;
    }
}





