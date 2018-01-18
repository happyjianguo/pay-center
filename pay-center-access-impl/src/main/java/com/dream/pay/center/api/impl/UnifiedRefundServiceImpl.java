package com.dream.pay.center.api.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.dream.pay.bean.DataResult;
import com.dream.pay.center.api.facade.UnifiedRefundService;
import com.dream.pay.center.api.request.RefundApplyRequest;
import com.dream.pay.center.api.request.RefundQueryRequest;
import com.dream.pay.center.api.response.RefundApplyResult;
import com.dream.pay.center.api.response.RefundQueryResult;
import com.dream.pay.center.core.refund.handler.RefundApplyHandler;
import com.dream.pay.center.service.handler.HandlerFactory;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Resource;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

/**
 * Created by mengzhenbin on 2017/10/13.
 */
@Path("/refund")
@Slf4j
public class UnifiedRefundServiceImpl implements UnifiedRefundService {

    //处理器工厂
    @Resource
    private HandlerFactory handlerFactory;

    @POST
    @Path("/apply")
    @Override
    public DataResult<RefundApplyResult> refundApply(RefundApplyRequest refundApplyRequest) {

        log.info("[退款申请]-受理请求,req={}", refundApplyRequest);

        DataResult<RefundApplyResult> response =
                handlerFactory.getHandler(RefundApplyHandler.class).handle(refundApplyRequest);

        log.info("[退款申请]-处理结果,resp={}", response);
        return response;
    }


    @POST
    @Path("/query")
    @Override
    public DataResult<RefundQueryResult> refundQuery(RefundQueryRequest refundQueryRequest) {
        return null;
    }
}