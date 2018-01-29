package com.dream.pay.center.model;

import lombok.*;

import java.util.Date;

@Data
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FundsRefundJobEntity extends BaseEntity{
    private Long id;

    private String refundDetailNo;

    private String payTradeItemsNo;

    private String payTradeNo;

    private Integer jobType;

    private String jobTypeDesc;

    private Integer jobStatus;

    private Integer jobRunCount;

    private String env;

    private Date createTime;

    private Date updateTime;

}