package com.dream.center.out.mock.dto;

import lombok.Getter;

/**
 * 操作状态类
 */
public enum OperationStatusEnum {
    PROCESSING(1, "处理中"),
    SUCCESS(2, "成功"),
    FAIL(3, "失败"),
    UNKNOW(4, "未知");

    @Getter
    private int code;
    @Getter
    private String desc;

    OperationStatusEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
