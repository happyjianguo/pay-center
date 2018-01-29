package com.dream.pay.center.model;

import lombok.*;

import java.util.Date;

@Data
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FundsWithdrawDetailEntity extends BaseEntity {
    private Long id;

    private String withdrawDetailNo;

    private String payTradeItemsNo;

    private String payTradeNo;

    private String bizTradeNo;

    private Integer currency;

    private Long withdrawAmount;

    private Integer withdrawStatus;

    private String channelReturnNo;

    private String outReturnNo;

    private Date outFinishTime;

    private String outErrorCode;

    private String outErrorMsg;

    private String userNo;

    private String merchantNo;

    private String partnerId;

    private Integer custAccountType;

    private String instId;

    private String instName;

    private String instBranchName;

    private String instProvince;

    private String instCity;

    private String instAccountNo;

    private String instAccountName;

    private String instAccountType;

    private String instCardType;

    private String withdrawTag;

    private String withdrawNote;

    private String outBizContext;

    private Date createTime;

    private Date updateTime;
}