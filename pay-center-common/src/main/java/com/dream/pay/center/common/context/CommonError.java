package com.dream.pay.center.common.context;

import lombok.Data;

import java.io.Serializable;

/**
 * 通用错误,描述一个错误需要具有的属性
 * <p>
 * Created by mengzhenbin on 16/7/17.
 */

@Data
public class CommonError implements Serializable {

    private static final long serialVersionUID = 8447004252990449418L;

    /**
     * 错误码
     */
    private String errorCode;

    /**
     * 错误信息
     */
    private String errorMessage;

    /**
     * 错误发生系统
     */
    private String location;

    public CommonError() {
    }

    public CommonError(String errorCode, String errorMessage, String location) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
        this.location = location;
    }

    /**
     * @see Object#toString()
     */
    @Override
    public String toString() {

        return location + "@" + errorCode + ":" + errorMessage;
    }

    /**
     * 转化为简单字符串表示。
     *
     * @return 转换结果
     */
    public String toDigest() {

        return errorCode + "@" + location;
    }
}
