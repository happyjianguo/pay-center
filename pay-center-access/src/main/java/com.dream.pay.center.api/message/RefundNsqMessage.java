package com.dream.pay.center.api.message;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 退款消息实体.
 *
 * @author mengzhenbin
 * @version RefundNsqMessage.java
 */
@Data
@Builder
public class RefundNsqMessage extends NsqEntity implements Serializable {

    private static final long serialVersionUID = 8513353130770135227L;

    /**
     * 产品码
     */
    private String bizProd;

    /**
     * 业务模式码
     */
    private String bizMode;

    /**
     * 业务流向码
     */
    private String bizAction;

    /**
     * 业务流向子码
     */
    private String bizSubAction;

    /**
     * 渠道码
     */
    private String refundChannel;

    /**
     * 外部退款单号
     */
    private String outBizNo;
    /**
     * 退款收单号
     */
    private String acquireNo;

    /**
     * 退款指令单号
     */
    private String refundDetailNo;

    /**
     * 原支付单号
     */
    private String payDetailNo;

    /**
     * 退款状态
     */
    private String refundStatus;

    /**
     * 退款金额
     */
    private long refundAmount;

    /**
     * 币种
     */
    private int currency;

    /**
     * 退款创建时间
     */
    private Date refundCreateTime;

    /**
     * 退款完成时间
     */
    private Date refundFinishTime;

    /**
     * 交易描述
     */
    private String tradeDesc;

}
