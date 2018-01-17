package com.dream.pay.center.status;

import com.dream.pay.center.api.enums.RefundResultEnum;
import com.dream.pay.center.api.enums.WithdrawResultEnum;
import lombok.Getter;


/**
 * 收单状态机
 * <p>
 * Created by mengzhenbin on 16/6/24.
 * <p>
 * 规范收单状态流转
 *
 * @see <http://doc.qima-inc.com/pages/viewpage.action?pageId=19366653/>
 * <p/>
 */
public enum FundsTradeStatus {


    /**
     * 创建状态，于支付Action【未支付】
     */
    CREATE(0, "已创建"),

    /**
     * 下层指令已触发，于支付Action【支付中】
     */
    PROCESSING(1, "交易处理中"),

    /**
     * 于支付Action【支付成功】
     */
    SUCCEED(2, "交易成功"),


    /**
     * 终止状态，于支付Action【支付失败】
     */
    FAILED(3, "交易失败"),

    /**
     * 终止状态，于支付Action【超时未付款关闭，支付全额退款成功】
     */
    CLOSE(4, "交易关闭"),

    /**
     * 发生交易逆向流程
     */
    REFUND(5, "转入退款"),

    /**
     * 于支付Action【已结算】
     */
    FINISHED(6, "交易完成");

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
    FundsTradeStatus(Integer code, String description) {
        this.code = code;
        this.description = description;
    }

    /**
     * 通过枚举<code>code</code>获得枚举
     *
     * @param code code
     * @return 支付收单状态机
     */
    public static FundsTradeStatus getByCode(Integer code) {
        for (FundsTradeStatus e : values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }

    /**
     * 是否支付完成
     *
     * @param status 收单状态
     */
    public static boolean isPayFinish(FundsTradeStatus status) {
        return status == FundsTradeStatus.SUCCEED || status == FundsTradeStatus.FAILED || status == FundsTradeStatus.FINISHED || status == FundsTradeStatus.CLOSE;
    }

    /**
     * 是否可以退款
     *
     * @param status 收单状态
     */
    public static boolean isCanRefundStatus(FundsTradeStatus status) {
        return status == FundsTradeStatus.SUCCEED || status == FundsTradeStatus.REFUND || status == FundsTradeStatus.FINISHED;
    }


    public static WithdrawResultEnum buildWithdrawStatus(FundsTradeStatus fundsTradeStatus) {
        switch (fundsTradeStatus) {
            case CREATE:
            case PROCESSING:
                return WithdrawResultEnum.WITHDRAW_PROCESSING;
            case SUCCEED:
            case CLOSE:
            case FINISHED:
                return WithdrawResultEnum.WITHDRAW_SUCCESS;
            case FAILED:
                return WithdrawResultEnum.WITHDRAW_SUCCESS;
        }
        return WithdrawResultEnum.WITHDRAW_UN_KNOW;
    }
}