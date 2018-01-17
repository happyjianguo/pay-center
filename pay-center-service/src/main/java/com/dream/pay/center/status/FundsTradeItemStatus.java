package com.dream.pay.center.status;

import com.dream.center.out.mock.dto.OperationStatusEnum;
import com.dream.pay.center.api.enums.RefundResultEnum;
import lombok.Getter;

/**
 * 业务子单状态
 * <p>
 * Created by mengzhenbin on 16/6/24.
 */
@Getter
public enum FundsTradeItemStatus {

    CREATE(0, "已创建"),

    CANCEL(4, "已取消"),

    DOING(1, "处理中"),

    SUCCESS(2, "处理成功"),

    FAILED(3, "处理失败"),;


    private final int code;
    private final String description;

    /**
     * 私有构造函数。
     *
     * @param code
     * @param description
     */
    FundsTradeItemStatus(int code, String description) {
        this.code = code;
        this.description = description;
    }

    /**
     * 通过枚举<code>code</code>获得枚举
     *
     * @param code
     * @return
     */
    public static FundsTradeItemStatus getByCode(int code) {
        for (FundsTradeItemStatus e : values()) {
            if (e.getCode() == code) {
                return e;
            }
        }
        return null;
    }

    public static FundsTradeItemStatus convertByOperateCode(OperationStatusEnum operationStatusEnum) {
        switch (operationStatusEnum) {
            case SUCCESS:
                return FundsTradeItemStatus.SUCCESS;
            case FAIL:
                return FundsTradeItemStatus.FAILED;
            case PROCESSING:
            case UNKNOW:
            default:
                return FundsTradeItemStatus.DOING;
        }
    }

    public static RefundResultEnum buildRefundStatus(FundsTradeItemStatus fundsTradeItemStatus) {
        switch (fundsTradeItemStatus) {
            case CREATE:
                return RefundResultEnum.REFUND_APPLY_SUCCESS;
            case CANCEL:
                return RefundResultEnum.REFUND_APPLY_FAIL;
            case DOING:
                return RefundResultEnum.REFUND_PROCESSING;
            case SUCCESS:
                return RefundResultEnum.REFUND_SUCCESS;
            case FAILED:
                return RefundResultEnum.REFUND_FAIL;
            default:
                return RefundResultEnum.REFUND_PROCESSING;
        }
    }
}
