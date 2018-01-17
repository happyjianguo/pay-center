package com.dream.pay.center.api.response;

import lombok.Data;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * 统一收单基础结果类
 *
 * @author mengzhenbin
 */
@Data
public class BusinessBaseResult implements Serializable {

    /**
     * 业务线-交易单号
     */
    private String bizTradeNo;

    /**
     * 收单-交易单号
     **/
    private String payTradeNo;

    /**
     * 扩展字段
     */
    private Map<String, String> extendInfo = new HashMap<String, String>();

}
