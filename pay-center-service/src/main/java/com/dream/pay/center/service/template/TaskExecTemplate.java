package com.dream.pay.center.service.template;

import com.dream.pay.center.service.context.FundsBaseContextHolder;
import com.dream.pay.center.common.constants.ErrorConstants;
import com.dream.pay.center.common.context.CommonError;
import com.dream.pay.center.common.enums.ErrorEnum;
import com.dream.pay.center.common.exception.BaseException;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.util.StopWatch;

/**
 * TASK 执行模板
 * 执行参数不需要为task的子类
 * <p>
 * Created by mengzhenbin on 16/6/27.
 */
@Slf4j
@Data
public class TaskExecTemplate<T> {

    private TransactionTemplate transactionTemplate;

    /**
     * 带事务执行
     *
     * @param task     请求对象
     * @param callback 对调接口
     * @return Task运行结果
     */
    public TemplateExecResult execWithTrans(T task, TemplateCallback callback) {
        return doExec(task, callback, true);
    }

    /**
     * 执行
     *
     * @param task      服务请求对象
     * @param callback  回调接口
     * @param withTrans 是否开启事务
     * @return 返回执行结果
     */
    private TemplateExecResult doExec(T task, final TemplateCallback<TemplateExecResult> callback,
                                      boolean withTrans) {
        TemplateExecResult result = new TemplateExecResult();

        if (log.isInfoEnabled()) {
            log.info("服务开始执行,task=" + task);
        }

        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
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
        } catch (BaseException e) {
            log.error("Task执行异常,task={}", task, e);
            result.setSuccess(false);
            result.getErrorContext().addError(new CommonError(e.getErrorEnum().getCode(),
                    e.getErrorEnum().getDescription(), ErrorConstants.EL_PAY_CENTER));
        } catch (Exception e) {
            log.error("Task执行异常,task=", task, e);
            result.setSuccess(false);
            result.getErrorContext().addError(new CommonError(ErrorEnum.UN_KNOW_EXCEPTION.getCode(),
                    ErrorEnum.UN_KNOW_EXCEPTION.getDescription(), ErrorConstants.EL_PAY_CENTER));
        } finally {
            if (log.isInfoEnabled()) {
                stopWatch.stop();
                log.info(
                        "服务结束执行,result=" + result + ",cast" + stopWatch.getTotalTimeMillis() + ",task=" + task);
            }
            //清除上下文,防止内存溢出
            FundsBaseContextHolder.clear();
        }
        return result;
    }

}
