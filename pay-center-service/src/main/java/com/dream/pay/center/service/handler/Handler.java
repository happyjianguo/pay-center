/**
 * Youzan.com Inc.
 * Copyright (c) 2012-2016 All Rights Reserved.
 */
package com.dream.pay.center.service.handler;


import com.dream.pay.bean.DataResult;

/**
 * 具体业务处理接口,范型化.
 * <p>
 * T: 入参类型
 * R: 返回结果类型
 *
 * @author mengzhenbin
 * @version Handler.java
 *          2016-12-21 21:08
 */
public interface Handler<T, R> {

    /**
     * 具体业务处理方法
     *
     * @param model 业务模型对象
     */
    DataResult<R> handle(T model);


}
