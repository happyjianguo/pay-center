package com.dream.center.out.mock.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 支付申请请求参数
 *
 * @author mengzhenbin
 * @version 1.0 on 2017/3/16
 */
@Data
@Setter
@Getter
@ToString(callSuper = true)
public class PayToolPayApplyRequest extends PayToolBaseRequest {

    /**
     * 外部业务号,收单系统上层业务系统单据号
     */
    private String outBizNo;

    /**
     * 收单号
     */
    private String payTradeNo;

    /**
     * 支付单据号
     */
    private String payTradeItemNo;

    /**
     * 支付工具单据号,即资产中心的资金明细单号
     */
    private String payDetailNo;

    /**
     * 支付总金额
     */
    private long totalAmount;

    /**
     * 商品信息
     */
    private String goodDesc;

    /**
     * 业务合作方商户号ID
     */
    private String partnerId;

    /**
     * 商户号
     */
    private String merchantNo;

    /**
     * 付款方ID
     */
    private String userNo;

    /**
     * 扩展字段
     */
    private Map<String, String> extend = new HashMap<>();

    /**
     * 业务透传上下文
     */
    private Map<String, String> outBizContext = new HashMap<>();

    /**
     * 交易超时时间
     */
    private Date expiredAt;

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
