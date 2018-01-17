package com.dream.pay.center.model;

import lombok.*;

import java.util.Date;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FundsAbnormalPayEntity extends BaseEntity{
    private Long id;

    private String bizTradeNo;

    private String payTradeNo;

    private String payTradeItemsNo;

    private String payDetailNo;

    private Integer type;

    private Integer status;

    private Date createTime;

    private Date updateTime;

}