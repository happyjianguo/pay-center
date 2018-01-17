package com.dream.pay.center.common.core;

import lombok.Data;

import java.io.Serializable;
import java.util.UUID;

/**
 * trace上下文,主要用来记录一次服务的调用链路
 *
 * Created by mengzhenbin on 16/9/2.
 */

@Data
public class TraceContext implements Serializable {

    private static final long serialVersionUID = -2255889720522464137L;

    /** 服务调用链路追踪id */
    private String traceId = UUID.randomUUID().toString().replaceAll("-", "");;

}
