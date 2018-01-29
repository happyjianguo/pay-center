package com.dream.pay.center.model;

import lombok.*;

import java.util.Date;

@Data
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FundsRefundDetailEntity extends BaseEntity{
    private Long id;

    private String refundDetailNo;

    private String payTradeItemsNo;

    private String payTradeNo;

    private String bizTradeNo;

    private Integer refundMode;

    private Integer currency;

    private Long refundAmount;

    private Integer refundStatus;

    private Long payAmount;

    private String payDetailNo;

    private String channelPayDetailNo;

    private String payTool;

    private String channelReturnNo;

    private String outReturnNo;

    private Date outFinishTime;

    private String outErrorCode;

    private String outErrorMsg;

    private String userNo;

    private String merchantNo;

    private String refundTag;

    private String refundNote;

    private String outBizContext;

    private Date createTime;

    private Date updateTime;
}