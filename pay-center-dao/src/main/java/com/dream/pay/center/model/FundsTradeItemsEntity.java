package com.dream.pay.center.model;

import lombok.*;

import java.util.Date;

@Data
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FundsTradeItemsEntity extends BaseEntity{
    private Integer id;

    private String payTradeItemsNo;

    private String payTradeNo;

    private String bizTradeNo;

    private Integer bizAction;

    private Integer mode;

    private Integer fundsInOut;

    private Long tradeAmount;

    private Long merchantNo;

    private String userNo;

    private Integer tradeState;

    private String tradeTag;

    private String tradeNote;

    private String outBizContext;

    private Date createTime;

    private Date updateTime;

}