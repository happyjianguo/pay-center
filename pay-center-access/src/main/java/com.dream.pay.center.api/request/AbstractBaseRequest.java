package com.dream.pay.center.api.request;

import com.dream.pay.center.common.core.TraceContext;
import com.dream.pay.center.common.exception.IllegalArgsException;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 请求参数基类
 *
 * @author mengzhenbin
 */
@Data
@Setter
@Getter
public abstract class AbstractBaseRequest implements Serializable {

    private static final long serialVersionUID = 8978692770109660222L;

    private TraceContext traceContext = new TraceContext();

    private String requestId;
}
