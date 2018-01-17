package com.dream.center.out.mock.dto;

import com.google.common.collect.Maps;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.validation.constraints.NotNull;
import java.util.Map;

/**
 * 退款申请请求参数
 *
 * @author mengzhenbin
 * @version 1.0 on 2017/3/16
 */
@Data
@Setter
@Getter
@ToString(callSuper = true)
public class PayToolRefundApplyRequest extends PayToolBaseRequest {

    /**
     * 支付工具返回的支付单号
     */
    private String payChannelDetailNo;

    /**
     * 退款对应的支付明细号
     */
    @NotNull(message = "支付明细号不能为空")
    private String payDetailNo;

    /**
     * 退款明细号
     */
    @NotNull(message = "支付总金额不能为空")
    private String refundDetailNo;

    /**
     * 订单总金额，单位为分，只能为整数
     */
    @NotNull(message = "支付总金额不能为空")
    private Long totalAmount;

    /**
     * 退款总金额，单位为分，只能为整数，可部分退款
     */
    @NotNull(message = "退款总金额不能为空")
    private Long refundAmount;

    /**
     * 扩展字段
     */
    private Map<String, String> extendedFields = Maps.newHashMap();

    /**
     * 业务透传上下文
     */
    private Map<String, String> outBizContext = Maps.newHashMap();

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
