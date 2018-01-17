package com.dream.pay.center.model;

import lombok.*;

import java.util.Date;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FundsPayDetailEntity extends BaseEntity implements Comparable<FundsPayDetailEntity> {
    private Long id;

    private String payDetailNo;

    private String payTradeItemsNo;

    private String payTradeNo;

    private String bizTradeNo;

    private String payTool;

    private Integer payToolType;

    private Integer bizSubAction;

    private Integer bizChannel;

    private Integer currency;

    private Long payAmount;

    private Long realAmount;

    private Integer payStatus;

    private String channelReturnNo;

    private String outReturnNo;

    private Date outFinishTime;

    private String outErrorCode;

    private String outErrorMsg;

    private String userNo;

    private String merchantNo;

    private String thirdMerchantNo;

    private String payTag;

    private String payNote;

    private String outBizContext;

    private Date createTime;

    private Date updateTime;

    public int compareTo(FundsPayDetailEntity detail) {
        return this.getPayToolType().compareTo(detail.getPayToolType());
    }
}