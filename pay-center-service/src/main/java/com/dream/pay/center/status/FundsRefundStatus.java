package com.dream.pay.center.status;

import lombok.Getter;

/**
 * 退款状态
 * <p>
 * Created by mengzhenbin on 16/6/24.
 */
public enum FundsRefundStatus {

    /**
     * 初始状态
     */
    APPLYING(0, "申请中"),

    /**
     * 调账务冻结失败
     */
    APPLY_FAIL(1, "申请失败"),

    /**
     * 调账务冻结成功
     */
    APPLY_SUCCESS(2, "申请成功"),

    /**
     * 调用渠道退款申请接口成功
     */
    PROCESSING(3, "退款处理中"),

    /**
     * 调用渠道退款申请／查询接口失败
     */
    EXCEPTION(4, "退款异常转人工处理"),

    /**
     * 调用渠道退款查询接口成功
     */
    SUCCESS(5, "退款成功");


    @Getter
    private int status;

    private String desc;


    FundsRefundStatus(int status, String desc) {
        this.status = status;
        this.desc = desc;
    }


    public static FundsRefundStatus getByCode(Integer state) {
        if (null == state) {
            return null;
        }
        for (FundsRefundStatus refundStatus : values()) {
            if (state.equals(refundStatus.getStatus())) {
                return refundStatus;
            }
        }
        return null;
    }
}





