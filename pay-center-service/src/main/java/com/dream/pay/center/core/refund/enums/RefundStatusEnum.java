package com.dream.pay.center.core.refund.enums;

import com.dream.pay.center.api.enums.RefundResultEnum;
import lombok.Getter;

/**
 * 退款状态枚举
 *
 * @author mengzhenbin
 * @since 2017-03-20
 */
public enum RefundStatusEnum {

    APPLYING(0, "退款申请中"),

    APPLY_SUCCESS(1, "退款申请成功"),

    APPLY_FAIL(2, "退款申请失败"),

    PROCESSING(3, "退款处理中"),

    EXCEPTION(4, "退款异常转人工"),

    SUCCESS(5, "退款成功");

    @Getter
    private int status;
    @Getter
    private String desc;

    RefundStatusEnum(int status, String desc) {
        this.status = status;
        this.desc = desc;
    }


    public static RefundStatusEnum getByCode(Integer state) {
        if (null == state) {
            return null;
        }
        for (RefundStatusEnum withdrawStatus : values()) {
            if (state.equals(withdrawStatus.getStatus())) {
                return withdrawStatus;
            }
        }
        return null;
    }

    public static RefundResultEnum convertToRefundResult(RefundStatusEnum refundStatusEnum) {
        switch (refundStatusEnum) {
            case APPLY_FAIL:
                return RefundResultEnum.REFUND_FAIL;
            case APPLYING:
            case PROCESSING:
            case APPLY_SUCCESS:
            case EXCEPTION:
                return RefundResultEnum.REFUND_PROCESSING;
            case SUCCESS:
                return RefundResultEnum.REFUND_SUCCESS;
        }
        return RefundResultEnum.REFUND_UN_KNOW;
    }
}
