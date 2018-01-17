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
public enum ChannelType {

    /**
     * 财付通
     **/
    TENPAY(5001, "财付通"),
    /**
     * 支付宝
     **/
    ALIPAY(5002, "支付宝"),
    /**
     * 微信
     **/
    WX(5003, "微信"),

    /**
     * 易宝
     **/
    YEEPAY(5004, "易宝"),

    /**
     * 联动优势
     **/
    UMP(5005, "联动优势"),

    /**
     * 百度
     **/
    BAIDU(5006, "百度"),

    /**
     * 中信银行
     **/
    CITIC(5007, "中信银行"),

    /**
     * 招商银行
     **/
    CMB(5008, "招商银行"),

    /**
     * 梦想
     **/
    DREAM(5010, "梦想");


    /**
     * 支付渠道码
     */
    @Getter
    private Integer code;

    /**
     * 描述
     */
    @Getter
    private String desc;

    /**
     * @param code 支付渠道码
     * @param desc 描述
     */
    ChannelType(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    /**
     * 根据名称获取枚举
     *
     * @param name 编码
     */
    public static ChannelType select(String name) {
        for (ChannelType ch : values()) {
            if (ch.name().equals(name)) {
                return ch;
            }
        }
        return null;
    }
}
