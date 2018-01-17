package com.dream.pay.center.common.exception;

import com.dream.pay.center.common.enums.ErrorEnum;

/**
 * 参数处理异常类。
 * <p>
 * Created by mengzhenbin on 16/7/1.
 */
public class IllegalArgsException extends BaseException {


    /**
     * 构造器
     */
    public IllegalArgsException() {
    }

    /**
     * 构造器
     *
     * @param errorEnum 错误码枚举
     */
    public IllegalArgsException(ErrorEnum errorEnum) {
        super(errorEnum);
    }

    /**
     * 构造器
     *
     * @param errorEnum    错误码枚举
     * @param errorMessage 错误描述
     */
    public IllegalArgsException(ErrorEnum errorEnum, String errorMessage) {
        super(errorEnum, errorMessage);
    }

    /**
     * 构造器
     *
     * @param errorEnum 错误码枚举
     * @param cause     异常
     */
    public IllegalArgsException(ErrorEnum errorEnum, Throwable cause) {
        super(errorEnum, cause);
    }

    /**
     * 构造器
     *
     * @param errorEnum    错误码枚举
     * @param errorMessage 错误描述
     * @param cause        异常
     */
    public IllegalArgsException(ErrorEnum errorEnum, String errorMessage, Throwable cause) {
        super(errorEnum, errorMessage, cause);
    }
}
