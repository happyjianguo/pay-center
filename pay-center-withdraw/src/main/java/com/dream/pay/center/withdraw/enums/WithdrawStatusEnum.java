package com.dream.pay.center.withdraw.enums;

import com.dream.pay.center.api.enums.WithdrawResultEnum;
import lombok.Getter;

/**
 * 提现状态枚举
 *
 * @author mengzhenbin
 * @since 2017-03-20
 */
public enum WithdrawStatusEnum {

    APPLYING(0, "提现申请中"),

    APPLY_SUCCESS(1, "提现申请成功"),

    APPLY_FAIL(2, "提现申请失败"),

    PROCESSING(3, "提现处理中"),

    RISK_FAIL(4, "风控拦截"),

    EXCEPTION(5, "提现异常转人工"),

    FAIL(6, "提现失败"),

    SUCCESS(7, "提现成功");

    @Getter
    private int status;
    @Getter
    private String desc;

    WithdrawStatusEnum(int status, String desc) {
        this.status = status;
        this.desc = desc;
    }


    public static WithdrawStatusEnum getByCode(Integer state) {
        if (null == state) {
            return null;
        }
        for (WithdrawStatusEnum withdrawStatus : values()) {
            if (state.equals(withdrawStatus.getStatus())) {
                return withdrawStatus;
            }
        }
        return null;
    }

    public static WithdrawResultEnum convertToWithdrawResult(WithdrawStatusEnum withdrawStatus) {
        switch (withdrawStatus) {
            case APPLY_FAIL:
            case RISK_FAIL:
            case FAIL:
                return WithdrawResultEnum.WITHDRAW_FAIL;
            case APPLYING:
            case PROCESSING:
            case APPLY_SUCCESS:
            case EXCEPTION:
                return WithdrawResultEnum.WITHDRAW_PROCESSING;
            case SUCCESS:
                return WithdrawResultEnum.WITHDRAW_SUCCESS;
        }
        return WithdrawResultEnum.WITHDRAW_UN_KNOW;
    }
}
