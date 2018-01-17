package com.dream.pay.center.api.request;

import com.dream.pay.center.common.enums.CurrencyCode;
import com.dream.pay.center.common.enums.PayTool;
import com.dream.pay.center.common.enums.PayToolType;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

/**
 * Created by socket on 2017/10/27.
 */
@Setter
@Getter
public class PayDetailRequest {
    /**
     * 支付工具类型
     */
    @NotNull(message = "支付工具类型不能为空")
    public PayToolType payToolType;

    /**
     * 支付工具
     */
    @NotNull(message = "支付工具不能为空")
    public PayTool payTool;

    /**
     * 活动编码(若为活动支付，则包含此值)
     */
    private Long activityId;

    /**
     * 支付金额
     */
    @NotNull(message = "支付金额不能为空")
    public long payAmount;

    /**
     * 币种：目前默认为人民币
     */
    @NotNull(message = "币种不能为空")
    private String currencyCode = CurrencyCode.CNY.getNum();

    /**
     * 资金明细备注信息,此信息将作为支付工具备注
     */
    private String memo;
}
