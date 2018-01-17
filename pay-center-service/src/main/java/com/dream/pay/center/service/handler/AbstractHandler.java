package com.dream.pay.center.service.handler;

import com.dream.pay.bean.DataResult;
import com.dream.pay.bean.ValidationResult;
import com.dream.pay.center.component.NsqMessagePoser;
import com.dream.pay.center.service.context.FundsBaseContextHolder;
import com.dream.pay.center.common.core.ResponseBuilder;
import com.dream.pay.center.common.enums.ErrorEnum;
import com.dream.pay.center.common.exception.BusinessException;
import com.dream.pay.center.common.exception.IllegalArgsException;
import com.dream.pay.utils.TracerContextUtils;
import com.dream.pay.utils.ValidationUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.StopWatch;

/**
 * 业务处理器模版,提供前处理,后处理等模版方法.
 * <p>
 * <pre>
 * 可以拿到子类的范型类型,反射进行数据与对象的转化
 *
 *
 * </pre>
 *
 * @author saber
 * @version AbstractHandler.java
 *          2016-12-21 21:08
 */
@Slf4j
public abstract class AbstractHandler<Req, R> extends NsqMessagePoser
        implements Handler<Req, R> {

    /**
     * 具体业务方法,业务具体类必须实现此方法
     *
     * @param request 请求对象
     */

    protected abstract R execute(Req request);


    /**
     * 模版方法,捕捉所有异常
     *
     * @param request 请求对象
     */
    public DataResult<R> handle(Req request) {
        StopWatch duration = new StopWatch();
        duration.start();
        R result = null;
        try {
            //1.生成调用链跟踪ID
            genTracerId(request);

            //2.前处理
            doBefore(request);

            //3.执行具体业务
            result = doExecute(request);

            //4.后处理
            doAfter(request, result);

            //5. 构造响应对象返回
            return ResponseBuilder.buildSuccessResp(result);

        } catch (IllegalArgumentException e) {
            log.warn("[处理器]-参数异常,req={}", request, e);
            DataResult<R> resp = ResponseBuilder.buildFailureResp(ErrorEnum.ARGUMENT_INVALID);
            if (e instanceof IllegalArgumentException) {
                resp.setMessage("null".equals(e.getMessage()) ?
                        ErrorEnum.ARGUMENT_INVALID.getDescription() : e.getMessage());//参数错误时，返回具体的错误信息
            } else {
                String msg = ((IllegalArgumentException) e).getMessage();
                resp.setMessage(msg);
            }
            return resp;
        } catch (BusinessException e) {
            log.error("[处理器]-业务异常,req={}", request, e);
            return ResponseBuilder.buildFailureResp(e.getErrorEnum());
        } catch (Exception e) {
            log.error("[处理器]-未知异常,req={}", request, e);
            return ResponseBuilder.buildFailureResp(ErrorEnum.UN_KNOW_EXCEPTION);
        } finally {
            log.info("[{},{}]", this.getClass().getSimpleName(), duration.getTime());
            duration.stop();
            //清除调用链跟踪ID
            TracerContextUtils.clearTracerId();
            //清除收单上下文
            FundsBaseContextHolder.clear();
        }
    }


    /**
     * 前处理方法,比如校验参数等
     *
     * @param request 请求对象
     */
    protected void doBefore(Req request) {
        //参数校验
        validate(request);
    }

    /**
     * execute 的包装方法.
     */
    protected R doExecute(Req model) {
        return execute(model);
    }


    /**
     * 后处理,比如清理资源,记录日志等.
     *
     * @param request 请求对象
     */
    protected void doAfter(Req request, R result) {
        //do nothing default
        log.debug("后处理,req={}", request);
    }


    /**
     * 生成tracerId
     *
     * @param request 请求对象
     */
    protected void genTracerId(Req request) {
        //生成调用链TracerId
        TracerContextUtils.genTracerId();
    }

    /**
     * 参数校验，基于JSR-349规范哦。
     *
     * @param request 请求参数
     */
    protected void validate(Req request) {
        ValidationResult result = ValidationUtils.validate(request);
        if (!result.isSuccess()) {
            throw new IllegalArgumentException(result.getMessage());
        }
    }

}


