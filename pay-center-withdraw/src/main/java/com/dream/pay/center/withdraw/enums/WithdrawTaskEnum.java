package com.dream.pay.center.withdraw.enums;


import lombok.Getter;

/**
 * 提现任务枚举
 *
 * @author mengzhenbin
 * @since 2017-03-21
 */
public enum WithdrawTaskEnum {

    INVOKE_ACCOUNT_TRANS_OUT(0, "调用账务冻结"),
    INVOKE_CHANNEL_WITHDRAW_APPLY(1, "调用渠道提现申请"),
    INVOKE_CHANNEL_WITHDRAW_QUERY(2, "调用渠道提现查询"),
    INVOKE_ACCOUNT_TRANS_IN(3, "调用账务解冻"),
    EXCEPTION_SUSPENDED(4, "异常挂起"),
    WITHDRAW_FAIL(5, "提现失败");

    @Getter
    private Integer code;
    @Getter
    private String desc;

    WithdrawTaskEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

}
