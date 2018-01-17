package com.dream.center.out.mock.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 支付查询请求参数
 *
 * @author mengzhenbin
 * @version 1.0 on 2017/3/16
 */
@Data
@Setter
@Getter
@ToString(callSuper = true)
public class PayToolPayQueryRequest extends PayToolBaseRequest {

    /**
     * 收单号
     */
    private String payTradeNo;

    /**
     * 支付工具单据号
     */
    private String payTradeItemNo;

    /**
     * 支付明细单号
     */
    private String payDetailNo;

}
