package com.dream.pay.center.api.impl;

import com.dream.center.out.mock.dto.PayOperationResult;
import com.dream.pay.bean.DataResult;
import com.dream.pay.center.api.facade.UnifiedPayService;
import com.dream.pay.center.api.request.PaySubmitRequest;
import com.dream.pay.center.api.response.PaySubmitResult;
import com.dream.pay.center.core.pay.handler.PayNotifyHandler;
import com.dream.pay.center.core.pay.handler.PaySubmitHandler;
import com.dream.pay.center.service.handler.HandlerFactory;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Resource;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

/**
 * 统一支付服务实现类。
 *
 * @author mengzhenbin
 */
@Path("/pay")
@Slf4j
public class UnifiedPayServiceImpl implements UnifiedPayService {

    //处理器工厂
    @Resource
    private HandlerFactory handlerFactory;


    @POST
    @Path("/submit")
    public DataResult<PaySubmitResult> paySubmit(PaySubmitRequest paySubmitRequest) {
        log.info("[支付提交]-受理请求,req={}", paySubmitRequest);

        DataResult<PaySubmitResult> response =
                handlerFactory.getHandler(PaySubmitHandler.class).handle(paySubmitRequest);

        log.info("[支付提交]-处理结果,resp={}", response);
        return response;
    }

    @POST
    @Path("/notify")
    public void payNotify(PayOperationResult payResult) {
        log.info("[支付通知]-受理请求,req={}", payResult);

        DataResult<Boolean> doneStatus = handlerFactory.getHandler(PayNotifyHandler.class).handle(payResult);

        log.info("[支付通知]-处理结果,resp={}", doneStatus);

    }
}