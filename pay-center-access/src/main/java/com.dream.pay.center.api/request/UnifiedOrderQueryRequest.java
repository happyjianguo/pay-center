/**
 * Youzan.com Inc.
 * Copyright (c) 2012-2017 All Rights Reserved.
 */
package com.dream.pay.center.api.request;

import com.dream.pay.bean.ValidationResult;
import com.dream.pay.center.common.enums.ErrorEnum;
import com.dream.pay.center.common.exception.IllegalArgsException;
import com.dream.pay.utils.ValidationUtils;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 查询统一收单请求类:包含外部订单号等属性
 *
 * @author mengzhenbin
 */
@Data
@Setter
@Getter
@ToString(callSuper = true)
public class UnifiedOrderQueryRequest extends AbstractBaseRequest {

    /**
     * 业务线-交易单号
     **/
    private String bizTradeNo;

    /**
     * 收单-交易单号
     **/
    private String payTradeNo;

    /**
     * 收单-交易子单号
     **/
    private String payDetailNo;
}
