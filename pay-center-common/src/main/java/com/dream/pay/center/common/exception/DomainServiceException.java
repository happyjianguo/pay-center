package com.dream.pay.center.common.exception;

import com.dream.pay.center.common.enums.ErrorEnum;

/**
 * 领域服务异常类。
 * <p>
 * Created by mengzhenbin on 16/7/1.
 */
public class DomainServiceException extends BaseException {

    /**
     * 构造器
     */
    public DomainServiceException() {
    }

    /**
     * 构造器
     *
     * @param errorEnum 错误码枚举
     */
    public DomainServiceException(ErrorEnum errorEnum) {
        super(errorEnum);
    }

    /**
     * 构造器
     *
     * @param errorEnum    错误码枚举
     * @param errorMessage 错误描述
     */
    public DomainServiceException(ErrorEnum errorEnum, String errorMessage) {
        super(errorEnum, errorMessage);
    }

    /**
     * 构造器
     *
     * @param errorEnum 错误码枚举
     * @param cause     异常
     */
    public DomainServiceException(ErrorEnum errorEnum, Throwable cause) {
        super(errorEnum, cause);
    }

    /**
     * 构造器
     *
     * @param errorEnum    错误码枚举
     * @param errorMessage 错误描述
     * @param cause        异常
     */
    public DomainServiceException(ErrorEnum errorEnum, String errorMessage, Throwable cause) {
        super(errorEnum, errorMessage, cause);
    }
}
