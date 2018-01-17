package com.dream.pay.center.common.core;

import com.dream.pay.bean.DataResult;
import com.dream.pay.center.common.enums.ErrorEnum;
import com.dream.pay.utils.TracerContextUtils;

/**
 * 响应实例构造器
 *
 * @author mengzhenbin
 * @version ResponseBuilder.java
 *          2016-12-23 16:46
 */
public class ResponseBuilder {


    /**
     * 构造成功的响应实例
     *
     * @param result 结果对象
     * @param <T>    结果类型
     */
    public static <T> DataResult<T> buildSuccessResp(T result) {
        return buildResp(ErrorEnum.SUCCESS, result, true);
    }

    /**
     * 构造失败的响应实例
     *
     * @param errorEnum 结果码
     * @param <T>       结果类型
     */
    public static <T> DataResult<T> buildFailureResp(ErrorEnum errorEnum) {
        return buildFailureResp(errorEnum, null);
    }

    /**
     * 构造失败的响应实例
     *
     * @param errorEnum 结果码
     * @param result    结果对象
     * @param <T>       结果类型
     */
    public static <T> DataResult<T> buildFailureResp(ErrorEnum errorEnum, T result) {
        return buildResp(errorEnum, result, false);
    }

    /**
     * 构造响应实例
     *
     * @param errorEnum 结果码
     * @param result    结果对象
     * @param success   是否成功
     * @param <T>       结果类型
     */
    public static <T> DataResult<T> buildResp(ErrorEnum errorEnum, T result, boolean success) {
        DataResult<T> resp = new DataResult<T>();
        resp.setSuccess(success);
        resp.setCode(errorEnum.getCode());
        resp.setMessage(errorEnum.getDescription());
        resp.setData(result);
        resp.setRequestId(getRespId());
        resp.setResponseId(getRespId());
        return resp;
    }

    //响应ID
    private static String getRespId() {
        return TracerContextUtils.getTracerId();
    }
}
