/*
 * Youzan.com Inc.
 * Copyright (c) 2012-2017 All Rights Reserved.
 */

package com.dream.pay.center.common.enums;

import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

/**
 * 支付单标签。
 *
 * @author mengzhenbin
 * @version PayTagEnum.java,
 * @since 2017-03-21 16:39
 */
public enum PayTagEnum {

    /**
     * 重复支付
     **/
    REPEATED_PAY(1, "重复支付"),

    /**
     * 组合支付中存在支付失败子单
     */
    PART_PAY(2, "部分支付"),

    /**
     * 改价少付
     */
    LESS_PAY(3, "少付"),

    /**
     * 改价超付
     */
    OVER_PAY(4, "超付");

    @Getter
    private final String desc;
    @Getter
    private final Integer code;

    /**
     * 私有构造函数。
     *
     * @param desc 描述
     */
    PayTagEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }


    /**
     * 通过枚举<code>code</code>获得枚举
     *
     * @param name name
     * @return 类型枚举
     */
    public static PayTagEnum select(String name) {
        for (PayTagEnum e : values()) {
            if (e.name().equals(name)) {
                return e;
            }
        }
        return null;
    }


    public static boolean existsTheName(String name) {
        if (StringUtils.isBlank(name)) return Boolean.FALSE;
        return PayTagEnum.select(name) != null;
    }
}
