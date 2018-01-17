package com.dream.pay.center.common.enums;

/**
 * 错误码枚举
 * Created by mengzhenbin on 16/6/27.
 */

public enum ErrorEnum {

    SUCCESS("S0000000", "受理成功"),

    /**
     * 基础部分错误码
     */
    SYSTEM_ERROR("E1000000", "系统内部错误"),
    IDEMPOTENT_EXCEPTION("E1000001", "幂等性检查异常"),
    UN_KNOW_EXCEPTION("E1000002", "未知异常"),
    ARGUMENT_INVALID("E1000003", "参数异常"),

    /**
     * 支付部分错误码
     */
    BIZ_ORDER_NOT_EXIST("E2000001", "业务单不存在"),
    PAY_ORDER_NO_ERROR("E2000001", "支付收单号不合法"),
    PAY_MODE_NOT_SUPPORT("E2000002", "支付模式不支持"),
    PAY_AMOUNT_ERROR("E2000003", "支付金额不合法"),
    PAY_STATUS_ERROR("E2000004","此单已支付成功，请勿重复提交支付" ),


    /**
     * 退款部分错误码
     */
    CHANNEL_NOT_SUPPORT("E3000001", "暂不支持此渠道退款"),
    REPEAT_REQUEST("E3000002", "退款申请请求重复"),
    PAY_ORDER_NOT_EXIST("E3000003", "支付单不存在"),
    PAY_STATUS_INVALID("E3000004", "支付单状态暂不支持退款操作"),
    REFUND_AMOUNT_INVALID("E3000005", "退款金额非法或已超退"),
    REFUND_PRE_TRANS_OUT_ERROR("E3000006", "提现预处理出账失败,请确认余额足够后重新发起"),

    /**
     * 提现部分错误码
     */
    WITHDRAW_AMOUNT_INVALID("E4000001", "提现金额非法"),
    QUERY_CARD_INFO_ERROR("E4000002", "获取银行卡信息失败,请联系系统管理员"),
    RISK_WITHDRAW_APPLY("E4000003", "账号存在风险,锁定提现功能"),
    WITHDRAW_PRE_TRANS_OUT_ERROR("E4000004", "提现预处理出账失败,请确认余额足够后重新发起"),
    WITHDRAW_FAIL_TRANS_IN_ERROR("E4000005", "提现失败入账处理失败,请联系系统管理员"), ;

    private final String code;
    private final String description;

    /**
     * 私有构造函数。
     *
     * @param code        code
     * @param description 描述
     */
    ErrorEnum(String code, String description) {
        this.code = code;
        this.description = description;
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

    /**
     * 通过枚举<code>code</code>获得枚举
     *
     * @param code code
     * @return 返回错误枚举
     */
    public static ErrorEnum getByCode(String code) {
        for (ErrorEnum errorEnum : values()) {
            if (errorEnum.getCode().equals(code)) {
                return errorEnum;
            }
        }
        return null;
    }

}
