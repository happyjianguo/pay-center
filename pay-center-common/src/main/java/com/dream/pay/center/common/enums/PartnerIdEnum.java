package com.dream.pay.center.common.enums;

import lombok.Getter;

/**
 * 业务线
 * <p>
 * Created by mengzhenbin on 16/7/21.
 */
public enum PartnerIdEnum {
    SHANGCHENG("1001", "有赞微商城"),
    XIAODIAN("1002", "有赞微小店"),
    SHOUYIN("1003", "有赞收银"),
    LINGSHOU("1004", "有赞零售"),
    MEIYE("1005", "有赞美业"),
    CANYIN("1006", "有赞餐饮"),
    PIFA("1007", "有赞批发"),
    SHOUKUAN("1008", "有赞收款");
    @Getter
    private final String code;
    @Getter
    private final String description;

    /**
     * 私有构造函数。
     *
     * @param code        code
     * @param description 描述
     */
    PartnerIdEnum(String code, String description) {
        this.code = code;
        this.description = description;
    }

    /**
     * 通过枚举<code>code</code>获得枚举
     *
     * @param code code
     * @return 类型枚举
     */
    public static PartnerIdEnum getByCode(String code) {
        for (PartnerIdEnum e : values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }


    /**
     * 通过枚举<code>code</code>获得枚举
     *
     * @param name name
     * @return 类型枚举
     */
    public static PartnerIdEnum select(String name) {
        for (PartnerIdEnum e : values()) {
            if (e.name().equals(name)) {
                return e;
            }
        }
        return null;
    }
}
