/*
 * Youzan.com Inc.
 * Copyright (c) 2012-2017 All Rights Reserved.
 */

package com.dream.pay.center.api.message;

import lombok.Data;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Nsq 消息实体.
 *
 * @author mengzhenbin
 * @version NsqEntity.java
 */
@Data
public class NsqEntity implements Serializable {

    /**
     * 消息来源的环境.
     **/
    private String srcEnv;

    /**
     * 消息头,k-v 格式
     **/
    private Map<String, String> headers = new HashMap<>();

}
