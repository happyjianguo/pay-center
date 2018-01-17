package com.dream.pay.center.service.template;

import com.dream.pay.center.service.context.FundsBaseContextHolder;
import com.dream.pay.center.api.request.AbstractBaseRequest;
import com.dream.pay.center.common.constants.ErrorConstants;
import com.dream.pay.center.common.context.CommonError;
import com.dream.pay.center.common.enums.ErrorEnum;
import com.dream.pay.center.common.exception.BaseException;
import com.dream.pay.center.common.exception.IllegalArgsException;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;


/**
 * Api 执行模板
 * <p>
 * Created by sinory on 16/6/27.
 */
@Slf4j
@Data
public class ApiExecTemplate<T extends AbstractBaseRequest> {

    private TransactionTemplate transactionTemplate;

    /**
     * 不带事务执行api
     *
     * @param request  请求对象
     * @param callback 回调接口
     * @return 返回执行结果
     */
    public TemplateExecResult execWithoutTrans(T request, TemplateCallback callback) {

        return doExec(request, callback, false);
    }

    /**
     * 带事务执行api
     *
     * @param request  请求对象
     * @param callback 对调接口
     * @return api运行结果
     */
    public TemplateExecResult execWithTrans(T request, TemplateCallback callback) {
        return doExec(request, callback, true);
    }

    /**
     * 执行
     *
     * @param request   服务请求对象
     * @param callback  回调接口
     * @param withTrans 是否开启事务
     * @return 返回执行结果
     */
    private TemplateExecResult doExec(T request, final TemplateCallback<TemplateExecResult> callback,
                                      boolean withTrans) {
        TemplateExecResult result = new TemplateExecResult();

        if (log.isInfoEnabled()) {
            log.info("服务开始执行,request={}", request);
        }

        long start = System.currentTimeMillis();

        try {
            if (withTrans) {
                result = this.transactionTemplate.execute(new TransactionCallback<TemplateExecResult>() {
                    /**
                     * @see TransactionCallback#doInTransaction(TransactionStatus)
                     */
                    public TemplateExecResult doInTransaction(TransactionStatus status) {
                        return callback.exec();
                    }
                });
            } else {
                result = callback.exec();
            }
        } catch (IllegalArgsException e) {
            log.warn("Api执行参数异常,request={}", request, e);
            result.setSuccess(false);
            result.getErrorContext().addError(new CommonError(e.getErrorEnum().getCode(),
                    e.getErrorEnum().getDescription(), ErrorConstants.EL_PAY_CENTER));
        } catch (BaseException e) {
            if (e.isNeedWarn()) {
                log.error("Api执行业务异常,request={}", request, e);
            } else {
                log.warn("Api执行异常,request={}", request, e);
            }
            result.setSuccess(false);
            result.getErrorContext().addError(new CommonError(e.getErrorEnum().getCode(),
                    e.getErrorEnum().getDescription(), ErrorConstants.EL_PAY_CENTER));
        } catch (Exception e) {
            log.error("Api执行未知异常,request={}", request, e);
            result.setSuccess(false);
            result.getErrorContext().addError(new CommonError(ErrorEnum.UN_KNOW_EXCEPTION.getCode(),
                    ErrorEnum.UN_KNOW_EXCEPTION.getDescription(), ErrorConstants.EL_PAY_CENTER));
        } finally {
            log.info("服务结束执行,cast={},result={},request={}", (System.currentTimeMillis() - start), result,
                    request);
            //清除上下文,防止内存溢出
            FundsBaseContextHolder.clear();
        }
        return result;
    }
}
