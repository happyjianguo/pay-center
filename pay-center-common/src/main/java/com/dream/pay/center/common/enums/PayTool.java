/**
 * Youzan.com Inc.
 * Copyright (c) 2012-2016 All Rights Reserved.
 */
package com.dream.pay.center.common.enums;

import lombok.Getter;

/**
 * 支付工具，给外部系统使用，会映射到我们内部API。
 *
 * @author mengzhenbin
 * @version PayTool.java
 *          2016-12-26 15:34
 */
public enum PayTool {

    /**
     * 微信-二维码支付
     **/
    WX_NATIVE(4011,PayToolType.PLAT_PAY, "微信二维码支付", true),
    /**
     * 微信-条码支付
     **/
    WX_BARCODE(4012,PayToolType.PLAT_PAY, "微信条码支付", true),
    /**
     * 微信-公众号支付，微信浏览器内调用JS发起的支付
     **/
    WX_JS(4013,PayToolType.PLAT_PAY, "微信公众号支付", true),
    /**
     * 微信-H5支付，非微信浏览器内调用发起的支付
     */
    WX_H5(4014,PayToolType.PLAT_PAY, "微信H5支付", true),
    /**
     * 微信-APP支付，唤起微信APP调用发起的支付
     */
    WX_APP(4015,PayToolType.PLAT_PAY, "微信APP支付", true),
    /**
     * 微信-小程序支付
     */
    WX_APPLET(4016,PayToolType.PLAT_PAY, "微信小程序支付", true),

    /**
     * 支付宝-二维码支付
     **/
    ALIPAY_NATIVE(4021,PayToolType.PLAT_PAY, "支付宝二维码支付", true),
    /**
     * 支付宝-条码支付
     **/
    ALIPAY_BARCODE(4022,PayToolType.PLAT_PAY, "支付宝条码支付", true),
    /**
     * 支付宝-WAP支付
     **/
    ALIPAY_WAP(4023,PayToolType.PLAT_PAY, "支付宝WAP支付", true),
    /**
     * 支付宝-APP支付
     **/
    ALIPAY_APP(4024,PayToolType.PLAT_PAY, "支付宝APP支付", true),


    /**
     * 借记卡支付
     */
    DEBIT_CARD(4031,PayToolType.BANK_PAY, "借记卡支付", true),
    /**
     * 贷记卡支付
     */
    CREDIT_CARD(4032,PayToolType.BANK_PAY, "贷记卡支付", true),


    /**
     * 通用预付卡
     */
    VALUE_CARD(4041,PayToolType.INNER_PAY, "储值卡支付"),
    /**
     * 余额支付
     */
    BALANCE(4042,PayToolType.INNER_PAY, "余额支付"),
    /**
     * 礼品卡
     */
    GIFT_CARD(4043,PayToolType.INNER_PAY, "礼品卡支付"),

    /**
     * 现金支付
     */
    CASH_PAY(4044,PayToolType.INNER_PAY, "现金支付"),



    /**
     * 立减
     */
    LI_JIAN(4051,PayToolType.ACTIVE_PAY, "立减活动支付"),
    /**
     * 满减
     */
    MAN_JIAN(4052,PayToolType.ACTIVE_PAY, "满减活动"),
    /**
     * 立返
     */
    LI_FAN(4053,PayToolType.ACTIVE_PAY, "立返活动支付"),

    /**
     * 满返
     */
    MAN_FAN(4054,PayToolType.ACTIVE_PAY, "满返活动"),


    /**
     * 中信提现
     */
    CITIC_WITHDRAW(4071,PayToolType.BANK_PAY, "中信提现"),
    /**
     * 财付通提现
     */
    TENPAY_WITHDRAW(4072,PayToolType.PLAT_PAY, "财付通提现");

    /**
     * 业务流向子码
     */
    @Getter
    private Integer bizSubAction;

    /**
     * 支付工具类型
     */
    @Getter
    private PayToolType payToolType;

    //描述
    @Getter
    private String desc;

    //body
    @Getter
    private boolean needBody;

    /**
     * 构造函数,默认不需要deeplink唤起密码输入框
     *
     * @param bizSubAction 业务流向子码
     * @param payToolType  支付工具类型
     * @param desc         描述
     */
    PayTool(Integer bizSubAction, PayToolType payToolType, String desc) {
        this(bizSubAction, payToolType, desc, false);
    }

    /**
     * 构造函数
     *
     * @param bizSubAction 业务流向子码
     * @param payToolType  支付工具类型
     * @param desc         描述
     * @param needBody     是否需要deeplink
     */
    PayTool(Integer bizSubAction, PayToolType payToolType, String desc, boolean needBody) {
        this.bizSubAction = bizSubAction;
        this.payToolType = payToolType;
        this.desc = desc;
        this.needBody = needBody;
    }

    /**
     * 根据名称获取枚举
     *
     * @param name 编码
     */
    public static PayTool select(String name) {
        for (PayTool ch : values()) {
            if (ch.name().equals(name)) {
                return ch;
            }
        }
        return null;
    }
}
