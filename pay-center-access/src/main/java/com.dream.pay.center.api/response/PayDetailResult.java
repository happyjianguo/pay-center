package com.dream.pay.center.api.response;

import com.dream.pay.center.common.enums.PayToolType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 提交支付支付详情处理类
 *
 * @author mengzhenbin
 */
@Setter
@Getter
@ToString
public class PayDetailResult {
    /**
     * 支付详情单号
     */
    public String payDetailNo;

    /**
     * 支付工具类型
     */
    public PayToolType payToolType;

    /**
     * 支付工具，(若为活动支付，则为活动编码)
     */
    public String payTool;

    /**
     * 支付金额
     */
    public long payAmount;

    /**
     * 支付状态
     */
    private String payStatus;

    /**
     * 主要针对第三方支付方式：封装跳转报文
     */
    private String body;

}
