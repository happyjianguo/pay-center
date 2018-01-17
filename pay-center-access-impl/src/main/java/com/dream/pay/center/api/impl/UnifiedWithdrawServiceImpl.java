package com.dream.pay.center.api.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.dream.pay.bean.DataResult;
import com.dream.pay.center.api.facade.UnifiedWithdrawService;
import com.dream.pay.center.api.request.WithdrawApplyRequest;
import com.dream.pay.center.api.request.WithdrawQueryRequest;
import com.dream.pay.center.api.response.WithdrawApplyResult;
import com.dream.pay.center.api.response.WithdrawQueryResult;
import com.dream.pay.center.service.handler.HandlerFactory;
import com.dream.pay.center.withdraw.handler.WithdrawApplyHandler;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Resource;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

/**
 * Created by mengzhenbin on 2017/10/13.
 */
@Path("/withdraw")
@Service(protocol = {"dubbo", "rest"}, registry = {"zookeeper"})
@Slf4j
public class UnifiedWithdrawServiceImpl implements UnifiedWithdrawService {

    //处理器工厂
    @Resource
    private HandlerFactory handlerFactory;

    @POST
    @Path("/apply")
    public DataResult<WithdrawApplyResult> withdrawApply(WithdrawApplyRequest withdrawApplyRequest) {

        log.info("[提现申请]-受理请求,req={}", withdrawApplyRequest);

        DataResult<WithdrawApplyResult> response =
                handlerFactory.getHandler(WithdrawApplyHandler.class).handle(withdrawApplyRequest);

        log.info("[提现申请]-处理结果,resp={}", response);
        return response;
    }

    @POST
    @Path("/query")
    public DataResult<WithdrawQueryResult> withdrawQuery(WithdrawQueryRequest withdrawQueryRequest) {
        return null;
    }
}
