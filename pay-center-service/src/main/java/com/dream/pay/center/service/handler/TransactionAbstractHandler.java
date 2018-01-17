
package com.dream.pay.center.service.handler;

import com.dream.pay.bean.DataResult;
import com.dream.pay.center.api.request.AbstractBaseRequest;
import com.dream.pay.utils.TracerContextUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import javax.annotation.Resource;

/**
 * 支持事务的处理器模版. <p> 提供事务包装方法. </p>
 *
 * @author mengzhenbin
 * @version TransactionAbstractHandler.java
 *          2016-12-22 16:12
 */
public abstract class TransactionAbstractHandler<Req extends AbstractBaseRequest, R>
        extends AbstractHandler<Req, R> {

    //本地事务模版
    @Resource
    protected TransactionTemplate transactionTemplate;

    /**
     * 事务包装方法,子类可以通过调用此方法来提供事务处理。
     *
     * @param action 事务action
     */
    protected <T> T wrapWithTransaction(TransactionCallback<T> action) {
        return transactionTemplate.execute(action);
    }

    @Override
    public DataResult<R> handle(Req request) {
        // 业务处理
        DataResult<R> response = super.handle(request);
        return response;
    }

    /**
     * 前处理方法,比如校验参数等
     *
     * @param request 请求对象
     */
    protected void genTracerId(Req request) {
        String requestId = request.getRequestId();
        if (StringUtils.isBlank(requestId)) {
            if (StringUtils.isBlank(request.getTraceContext().getTraceId())) {
                //生成调用链TracerId
                TracerContextUtils.genTracerId();
            } else {
                TracerContextUtils.setTracerId(request.getTraceContext().getTraceId());
            }
        } else {
            //使用requestId作为TracerId
            TracerContextUtils.setTracerId(requestId);
        }

    }
}
