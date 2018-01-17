package com.dream.pay.center.service.template;

/**
 * 模板执行回调服务
 *
 * Created by mengzhenbin on 16/6/27.
 */

public interface TemplateCallback<T> {

    /**
     * 执行回调
     */
    T exec();
}
