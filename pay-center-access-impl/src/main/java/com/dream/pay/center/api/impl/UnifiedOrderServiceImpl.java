package com.dream.pay.center.api.impl;

import com.dream.pay.bean.DataResult;
import com.dream.pay.center.api.facade.UnifiedOrderService;
import com.dream.pay.center.core.order.handler.UnifiedOrderCreateHandler;
import com.dream.pay.center.service.handler.HandlerFactory;
import com.dream.pay.center.api.request.UnifiedOrderCreateRequest;
import com.dream.pay.center.api.request.UnifiedOrderQueryRequest;
import com.dream.pay.center.api.response.UnifiedOrderCreateResult;
import com.dream.pay.center.api.response.UnifiedOrderQueryResult;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Resource;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

/**
 * 统一收单服务实现类。
 *
 * @author mengzhenbin
 */
@Path("/unifiedorder")
@Slf4j
public class UnifiedOrderServiceImpl implements UnifiedOrderService {

    //处理器工厂
    @Resource
    private HandlerFactory handlerFactory;


    @POST
    @Path("/create")
    public DataResult<UnifiedOrderCreateResult> create(final UnifiedOrderCreateRequest request) {
        log.info("[统一收单]-创建请求,req={}", request);

        DataResult<UnifiedOrderCreateResult> response =
                handlerFactory.getHandler(UnifiedOrderCreateHandler.class).handle(request);

        log.info("[统一收单]-创建结果,resp={}", response);
        return response;
    }


    @POST
    @Path("/query")
    public DataResult<UnifiedOrderQueryResult> query(UnifiedOrderQueryRequest request) {
        return null;
    }

}
