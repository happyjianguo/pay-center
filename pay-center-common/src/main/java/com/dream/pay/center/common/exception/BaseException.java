package com.dream.pay.center.common.exception;

import com.dream.pay.center.common.enums.ErrorEnum;
import lombok.Data;

/**
 * 基础异常类
 * <p>
 * Created by mengzhenbin on 16/6/27.
 */

@Data
public class BaseException extends RuntimeException {

    private static final long serialVersionUID = -321681548158747016L;

    /**
     * 是否需要报警,默认是需要报警
     */
    private boolean isNeedWarn = true;

    /**
     * 错误码枚举
     */
    private ErrorEnum errorEnum;

    /**
     * 创建一个<code>BaseException</code>对象
     */
    public BaseException() {
        super();
    }

    /**
     * 创建一个<code>BaseException</code>对象
     *
     * @param e 业务异常
     */
    public BaseException(BaseException e) {
        super(e);
        this.errorEnum = e.errorEnum;
    }

    /**
     * 创建一个<code>BaseException</code>
     *
     * @param errorEnum 错误枚举
     */
    public BaseException(ErrorEnum errorEnum) {
        super(errorEnum.getDescription());
        this.errorEnum = errorEnum;
    }

    /**
     * 创建一个<code>BaseException</code>
     *
     * @param errorEnum 错误枚举
     */
    public BaseException(ErrorEnum errorEnum, String errorMessage) {
        super(errorMessage);
        this.errorEnum = errorEnum;
    }

    /**
     * 创建一个<code>BaseException</code>
     *
     * @param errorEnum    错误枚举
     * @param errorMessage 错误信息
     * @param isNeedWarn   是否需要报警
     */
    public BaseException(ErrorEnum errorEnum, String errorMessage, boolean isNeedWarn) {
        super(errorMessage);
        this.errorEnum = errorEnum;
        this.isNeedWarn = isNeedWarn;
    }

    /**
     * 创建一个<code>BaseException</code>
     *
     * @param errorEnum 错误枚举
     * @param cause     异常
     */
    public BaseException(ErrorEnum errorEnum, Throwable cause) {
        super(errorEnum.getDescription(), cause);
        this.errorEnum = errorEnum;
    }

    /**
     * 创建一个<code>BaseException</code>
     *
     * @param errorEnum    错误枚举
     * @param errorMessage 错误描述
     * @param cause        异常
     */
    public BaseException(ErrorEnum errorEnum, String errorMessage, Throwable cause) {
        super(errorMessage, cause);
        this.errorEnum = errorEnum;
    }

}
