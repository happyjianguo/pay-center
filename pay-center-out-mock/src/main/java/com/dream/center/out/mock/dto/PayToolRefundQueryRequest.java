package com.dream.center.out.mock.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 退款查询请求参数
 *
 * @author mengzhenbin
 * @version 1.0 on 2017/3/16
 */
@Data
@Setter
@Getter
@ToString(callSuper = true)
public class PayToolRefundQueryRequest extends PayToolBaseRequest {
    /**
     * 收单支付明细号
     */
    private String payDetailNo;

    /**
     * 支付工具支付明细号
     */
    private String payChannelDetailNo;

    /**
     * 收单退款明细号
     */
    private String refundDetailNo;

    /**
     * 支付工具退款明细号
     */
    private String refundChannelDetailNo;

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
