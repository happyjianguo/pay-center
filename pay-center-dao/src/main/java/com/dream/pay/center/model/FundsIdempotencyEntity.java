package com.dream.pay.center.model;

import lombok.*;

import java.util.Date;


@Data
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FundsIdempotencyEntity extends BaseEntity {
    private Long id;

    private String idempotencyKey;

    private Date createTime;
}