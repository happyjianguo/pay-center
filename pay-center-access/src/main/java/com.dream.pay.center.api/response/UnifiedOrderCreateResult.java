package com.dream.pay.center.api.response;

import com.dream.pay.enums.CurrencyCode;
import lombok.Data;
import lombok.ToString;

/**
 * 统一收单创建结果
 *
 * @author mengzhenbin
 */
@Data
@ToString(callSuper = true)
public class UnifiedOrderCreateResult extends BusinessBaseResult {

    /**
     * 收单-交易状态
     */
    private String tradeStatus;

    /**
     * 交易金额
     */
    private long tradeAmount;

    /**
     * 币种：目前默认为人民币
     */
    private String currencyCode = CurrencyCode.CNY.getAlpha();
}
