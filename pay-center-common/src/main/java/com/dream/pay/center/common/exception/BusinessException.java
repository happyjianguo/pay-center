package com.dream.pay.center.common.exception;

import com.dream.pay.center.common.enums.ErrorEnum;
import lombok.Data;

/**
 * 业务处理异常类。
 * <p>
 * Created by mengzhenbin on 16/7/1.
 */
@Data
public class BusinessException extends BaseException {
    /**
     * 构造器
     */
    public BusinessException() {
    }

    /**
     * 构造器
     *
     * @param errorEnum 错误码枚举
     */
    public BusinessException(ErrorEnum errorEnum) {
        super(errorEnum);
    }

    /**
     * 构造器
     *
     * @param errorEnum    错误码枚举
     * @param errorMessage 错误描述
     */
    public BusinessException(ErrorEnum errorEnum, String errorMessage) {
        super(errorEnum, errorMessage);
    }

    /**
     * 构造器
     *
     * @param errorEnum 错误码枚举
     * @param cause     异常
     */
    public BusinessException(ErrorEnum errorEnum, Throwable cause) {
        super(errorEnum, cause);
    }

    /**
     * 构造器
     *
     * @param errorEnum    错误码枚举
     * @param errorMessage 错误描述
     * @param cause        异常
     */
    public BusinessException(ErrorEnum errorEnum, String errorMessage, Throwable cause) {
        super(errorEnum, errorMessage, cause);
    }
}
