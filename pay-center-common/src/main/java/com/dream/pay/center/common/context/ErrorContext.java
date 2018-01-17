package com.dream.pay.center.common.context;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 错误码上下文
 * <p>
 * Created by mengzhenbin on 16/7/1.
 */

@Data
public class ErrorContext implements Serializable {

    private static final long serialVersionUID = -7672946511702238432L;

    /**
     * 错误堆栈集合
     */
    private List<CommonError> errorStack = new ArrayList<CommonError>();

    /**
     * 第三方错误原始信息
     */
    private String thirdPartyError;

    /**
     * 默认分隔符
     */
    private static final String SPLIT = "|";

    /**
     * 默认构造方法
     */
    public ErrorContext() {
    }

    // ~~~ 公共方法

    /**
     * 获取当前错误对象
     *
     * @return 错误对象
     */
    public CommonError fetchCurrentError() {

        if (errorStack != null && errorStack.size() > 0) {

            return errorStack.get(errorStack.size() - 1);
        }
        return null;
    }

    /**
     * 获取当前错误码
     *
     * @return 当前错误码
     */
    public String fetchCurrentErrorCode() {

        if (errorStack != null && errorStack.size() > 0) {

            return errorStack.get(errorStack.size() - 1).getErrorCode().toString();
        }
        return null;
    }

    /**
     * 获取原始错误对象
     *
     * @return 原是错误对象
     */
    public CommonError fetchRootError() {

        if (errorStack != null && errorStack.size() > 0) {
            return errorStack.get(0);
        }
        return null;
    }

    /**
     * 向堆栈中添加错误对象。
     *
     * @param error 错误对象
     */
    public void addError(CommonError error) {

        if (errorStack == null) {

            errorStack = new ArrayList<CommonError>();
        }
        errorStack.add(error);
    }

    /**
     * 转化为简单字符串表示。
     *
     * @return 字符串结果
     */
    public String toDigest() {

        StringBuffer sb = new StringBuffer();

        for (int i = errorStack.size(); i > 0; i--) {

            if (i == errorStack.size()) {

                sb.append(digest(errorStack.get(i - 1)));
            } else {

                sb.append(SPLIT).append(digest(errorStack.get(i - 1)));
            }
        }
        return sb.toString();
    }

    /**
     * @see Object#toString()
     */
    @Override
    public String toString() {

        StringBuffer sb = new StringBuffer();

        for (int i = errorStack.size(); i > 0; i--) {

            if (i == errorStack.size()) {

                sb.append(errorStack.get(i - 1));
            } else {

                sb.append(SPLIT).append(errorStack.get(i - 1));
            }
        }
        return sb.toString();
    }

    /**
     * 获取错误对象简单表示
     *
     * @param commonError 错误对象
     * @return 错误对象String
     */
    private String digest(CommonError commonError) {

        if (null == commonError) {

            return null;
        }

        return commonError.toDigest();
    }

}
