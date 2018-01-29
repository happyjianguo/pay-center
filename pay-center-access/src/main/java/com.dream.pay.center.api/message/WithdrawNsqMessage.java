package com.dream.pay.center.api.message;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 提现消息实体.
 *
 * @author mengzhenbin
 * @version WithdrawNsqMessage.java
 */
@Data
@Builder
public class WithdrawNsqMessage extends NsqEntity implements Serializable {

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
    private String withdrawChannel;

    /**
     * 外部提现单号
     */
    private String outBizNo;
    /**
     * 提现收单号
     */
    private String acquireNo;

    /**
     * 提现提现指令单号
     */
    private String withdrawNo;

    /**
     * 商户号
     */
    private String merchantNo;

    /**
     * 合作方号
     */
    private String partnerId;

    /**
     * 提现状态
     */
    private String withdrawStatus;

    /**
     * 提现金额
     */
    private long withdrawAmount;

    /**
     * 币种
     */
    private int currency;

    /**
     * 提现创建时间
     */
    private Date withdrawCreateTime;

    /**
     * 提现完成时间
     */
    private Date withdrawFinishTime;

    /**
     * 交易描述
     */
    private String tradeDesc;


}
