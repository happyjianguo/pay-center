package com.dream.pay.center.core.pay.service.submit;

import com.dream.center.out.mock.dto.OperationStatusEnum;
import com.dream.center.out.mock.dto.PayOperationResult;
import com.dream.pay.center.api.response.PayDetailResult;
import com.dream.pay.center.api.response.PaySubmitResult;
import com.dream.pay.center.common.exception.BusinessException;
import com.dream.pay.center.core.pay.service.PayCoreService;
import com.dream.pay.center.model.FundsPayDetailEntity;
import com.dream.pay.center.service.context.FundsPayContext;
import com.dream.pay.center.service.out.ChannelService;
import com.dream.pay.enums.PayToolType;
import com.dream.pay.utils.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component("gatePaySubmitPayHandler")
@Slf4j
public class GatePaySubmitPayHandler implements SubmitPayHandler {

    @Resource
    protected ChannelService channelService;
    @Resource
    protected PayCoreService payCoreService;

    public void submitPay(FundsPayContext fundsPayContext, PaySubmitResult paySubmitResult) throws BusinessException {
        FundsPayDetailEntity currentDetail = fundsPayContext.getCurrentDetail();
        log.info("[网关支付]-[支付提交]-处理开始|本次处理支付单-{}", currentDetail);

        PayDetailResult payDetailResult = new PayDetailResult();

        payDetailResult.setPayAmount(currentDetail.getPayAmount());
        payDetailResult.setPayDetailNo(currentDetail.getPayDetailNo());
        payDetailResult.setPayTool(currentDetail.getPayTool());
        payDetailResult.setPayToolType(PayToolType.toPayToolType(currentDetail.getPayToolType()));

        PayOperationResult payOperationResult = channelService.payApply(currentDetail);//调用渠道获取跳转信息

        OperationStatusEnum operationStatusEnum = payOperationResult.getOperateResultCode();

        switch (operationStatusEnum) {
            case SUCCESS:
                payCoreService.updateTradeStatus(fundsPayContext);
                String body = JsonUtil.toJson(payOperationResult.getThreePartyReturnValue());
                payDetailResult.setBody(body);
                break;
            case PROCESSING:
            case UNKNOW:
            case FAIL:
                payDetailResult.setBody("");
            default:
        }
        payDetailResult.setPayStatus(String.valueOf(currentDetail.getPayStatus()));//封装最终支付单状态
        paySubmitResult.getPayDetailResultList().add(payDetailResult);
        log.info("[网关支付]-[支付提交]-处理结束|本次处理结果-{}", payDetailResult);
    }
}
