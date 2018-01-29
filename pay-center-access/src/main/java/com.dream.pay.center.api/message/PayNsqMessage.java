/*
 * Youzan.com Inc.
 * Copyright (c) 2012-2017 All Rights Reserved.
 */

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
public class PayNsqMessage extends NsqEntity implements Serializable {

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
    private String payChannel;

    /**
     * 外部支付单号
     */
    private String outBizNo;
    /**
     * 支付收单号
     */
    private String acquireNo;

    /**
     * 支付指令单号
     */
    private String payDetailNo;

    /**
     * 商户号
     */
    private String merchantNo;

    /**
     * 合作方号
     */
    private String partnerId;

    /**
     * 支付状态
     */
    private String payStatus;

    /**
     * 支付金额
     */
    private long payAmount;

    /**
     * 币种
     */
    private int currency;

    /**
     * 支付单创建时间
     */
    private Date payCreateTime;

    /**
     * 支付完成时间
     */
    private Date payFinishedTime;

    /**
     * 支付工具
     */
    private String payTool;

    /**
     * 第三方流水号
     */
    private String channelSettleNo;

    /**
     * 支付标签：比如，重复支付: REPEATED_PAY等。
     */
    private String payTag;

    /**
     * 交易描述
     */
    private String tradeDesc;

}
