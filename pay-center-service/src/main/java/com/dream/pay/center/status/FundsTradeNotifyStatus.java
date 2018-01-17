package com.dream.pay.center.status;

import lombok.Getter;

/**
 * 收单通知状态
 * <p>
 * Created by mengzhenbin on 16/6/24.
 * <p>
 * 规范收单状态流转
 *
 * @see <http://doc.qima-inc.com/pages/viewpage.action?pageId=19366653/>
 * <p/>
 */
public enum FundsTradeNotifyStatus {


    NONE(0, "未通知"),

    DONE(1, "已通知");

    @Getter
    private final Integer code;
    @Getter
    private final String description;

    /**
     * 私有构造函数。
     *
     * @param code        code
     * @param description 描述
     */
    FundsTradeNotifyStatus(Integer code, String description) {
        this.code = code;
        this.description = description;
    }

    /**
     * 通过枚举<code>code</code>获得枚举
     *
     * @param code code
     * @return 支付收单状态机
     */
    public static FundsTradeNotifyStatus getByCode(Integer code) {
        for (FundsTradeNotifyStatus e : values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }
}
