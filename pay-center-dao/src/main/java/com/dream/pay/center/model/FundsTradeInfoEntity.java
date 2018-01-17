package com.dream.pay.center.model;

import lombok.*;

import java.util.Date;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FundsTradeInfoEntity extends BaseEntity {
    private Integer id;

    private String payTradeNo;

    private String bizTradeNo;

    private String bizTradeName;

    private Integer bizProd;

    private Integer bizMode;

    private Integer bizAction;

    private Long merchantNo;

    private String userNo;

    private String userName;

    private String partnerId;

    private Integer currency;

    private Long tradeAmount;

    private Long realAmount;

    private Long refundAmount;

    private Long refundInprocessAmount;

    private Integer tradeState;

    private Integer settledState;

    private Integer notifyState;

    private Integer notifyCount;

    private Date paymentTime;

    private Date lastRefundTime;

    private Date expiredTime;

    private String extraInfo;

    private String outBizContext;

    private Date createTime;

    private Date updateTime;

}