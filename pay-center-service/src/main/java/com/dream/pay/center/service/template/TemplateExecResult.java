package com.dream.pay.center.service.template;

import com.dream.pay.center.common.context.ErrorContext;
import lombok.Data;

/**
 * 模板执行结果
 * <p>
 * Created by mengzhenbin on 16/7/15.
 */
@Data
public class TemplateExecResult<T> {

    /**
     * 是否执行成功
     */
    private boolean success;

    /***
     * 执行结果数据
     */
    private T data;

    /**
     * 错误上下文
     */
    private ErrorContext errorContext = new ErrorContext();

    /**
     * 空的构造函数
     */
    public TemplateExecResult() {
    }

    /**
     * 通过data构造一个<code>TemplateExecResult</code>实例
     *
     * @param data 执行返回的数据
     */
    public TemplateExecResult(T data) {
        this.success = true;
        this.data = data;
    }
}
