package com.dream.pay.center.model;

import lombok.*;

import java.util.Date;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FundsWithdrawJobEntity extends BaseEntity{
    private Long id;

    private String withdrawDetailNo;

    private String payTradeItemsNo;

    private String payTradeNo;

    private Integer jobType;

    private String jobTypeDesc;

    private Integer jobStatus;

    private Integer jobRunCount;

    private String jobLevel;

    private String env;

    private Date createTime;

    private Date updateTime;
}