package com.dream.center.out.mock.dto;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;

/**
 * 账务操作状态类
 */
@Setter
@Getter
@ToString(callSuper = true)
public class OperationBaseResult {

    /**
     * 调用是否成功
     */
    private boolean isSuccess;

    /**
     * 操作状态枚举
     */
    private OperationStatusEnum operateResultCode = OperationStatusEnum.UNKNOW;

    /**
     * 错误码
     */
    private String errorCode;

    /**
     * 错误信息
     */
    private String errorMessage;
}
