package com.dream.pay.center.common.exception;


import com.dream.pay.center.common.enums.ErrorEnum;

/**
 * 组件服务异常类。
 * <p>
 * Created by mengzhenbin on 16/7/1.
 */
public class ComponentException extends BaseException {

    private static final long serialVersionUID = 8722370821345073310L;

    /**
     * 构造器
     */
    public ComponentException() {
    }

    /**
     * 构造器
     *
     * @param errorEnum 错误码枚举
     */
    public ComponentException(ErrorEnum errorEnum) {
        super(errorEnum);
    }

    /**
     * 构造器
     *
     * @param errorEnum    错误码枚举
     * @param errorMessage 错误描述
     */
    public ComponentException(ErrorEnum errorEnum, String errorMessage) {
        super(errorEnum, errorMessage);
    }

    /**
     * 构造器
     *
     * @param errorEnum 错误码枚举
     * @param cause     异常
     */
    public ComponentException(ErrorEnum errorEnum, Throwable cause) {
        super(errorEnum, cause);
    }

    /**
     * 构造器
     *
     * @param errorEnum    错误码枚举
     * @param errorMessage 错误描述
     * @param cause        异常
     */
    public ComponentException(ErrorEnum errorEnum, String errorMessage, Throwable cause) {
        super(errorEnum, errorMessage, cause);
    }
}
