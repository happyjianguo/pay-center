package com.dream.pay.center.status;

import com.dream.center.out.mock.dto.OperationStatusEnum;
import com.dream.pay.center.model.FundsPayDetailEntity;
import lombok.Getter;

/**
 * 支付状态
 * Created by mengzhenbin on 16/6/24.
 */
public enum FundsPayStatus {
    /**
     * 未支付
     */
    NO_PAY(0, "未支付"),
    /**
     * 支付成功
     */
    SUCCESS(2, "支付成功"),
    /**
     * 支付失败
     */
    FAIL(1, "支付失败"),
    /**
     * 待退--发生部分支付／重复支付时需要标记为待退
     */
    //REFUND_TODO(3, "待退"),
    /**
     * 已退--部分支付／重复支付发生并且退款成功
     */
    //REFUND_DONE(4, "已退"),
    /**
     * 已经关闭
     */
    CLOSED(5, "支付关闭");

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
    FundsPayStatus(Integer code, String description) {
        this.code = code;
        this.description = description;
    }

    /**
     * 通过枚举<code>code</code>获得枚举
     *
     * @param code code
     * @return 支付收单状态机
     */
    public static FundsPayStatus getByCode(Integer code) {
        for (FundsPayStatus e : values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }

    public static FundsPayStatus convertByOperateCode(OperationStatusEnum operationStatusEnum) {
        switch (operationStatusEnum) {
            case SUCCESS:
                return FundsPayStatus.SUCCESS;
            case FAIL:
                return FundsPayStatus.FAIL;
            case PROCESSING:
            case UNKNOW:
            default:
                return FundsPayStatus.NO_PAY;
        }
    }


}

