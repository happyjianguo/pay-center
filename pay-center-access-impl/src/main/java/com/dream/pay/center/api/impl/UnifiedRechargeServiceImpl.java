package com.dream.pay.center.api.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.dream.pay.bean.DataResult;
import com.dream.pay.center.api.facade.UnifiedRechargeService;
import com.dream.pay.center.api.request.RechargeApplyRequest;
import com.dream.pay.center.api.response.RechargeApplyResult;
import com.dream.pay.center.core.recharge.handler.RechargeApplyHandler;
import com.dream.pay.center.service.handler.HandlerFactory;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Resource;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

/**
 * 统一收单服务实现类。
 *
 * @author mengzhenbin
 */
@Path("/recharge")
@Slf4j
public class UnifiedRechargeServiceImpl implements UnifiedRechargeService {

    //处理器工厂
    @Resource
    private HandlerFactory handlerFactory;


    @POST
    @Path("/apply")
    @Override
    public DataResult<RechargeApplyResult> rechargeApply(RechargeApplyRequest rechargeApplyRequest) {
        log.info("[充值申请]-创建请求,req={}", rechargeApplyRequest);

        DataResult<RechargeApplyResult> response =
                handlerFactory.getHandler(RechargeApplyHandler.class).handle(rechargeApplyRequest);

        log.info("[充值申请]-创建结果,resp={}", response);
        return response;
    }
}
