package com.dream.pay.center.core.pay.service.submit;

import com.dream.pay.center.api.response.PayDetailResult;
import com.dream.pay.center.api.response.PaySubmitResult;
import com.dream.pay.center.common.exception.BusinessException;
import com.dream.pay.center.model.FundsPayDetailEntity;
import com.dream.pay.center.service.context.FundsPayContext;
import com.dream.pay.enums.PayToolType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component("quickPaySubmitPayHandler")
@Slf4j
public class quickPaySubmitPayHandler implements SubmitPayHandler {

    public void submitPay(FundsPayContext fundsPayContext, PaySubmitResult paySubmitResult) throws BusinessException {
        FundsPayDetailEntity currentDetail = fundsPayContext.getCurrentDetail();
        log.info("[快捷支付]-[支付提交]-处理开始|本次处理支付单-{}", currentDetail);

        PayDetailResult payDetailResult = new PayDetailResult();

        payDetailResult.setPayAmount(currentDetail.getPayAmount());
        payDetailResult.setPayDetailNo(currentDetail.getPayDetailNo());
        payDetailResult.setPayTool(currentDetail.getPayTool());
        payDetailResult.setPayToolType(PayToolType.toPayToolType(currentDetail.getPayToolType()));

        payDetailResult.setPayStatus(String.valueOf(currentDetail.getPayStatus()));
        paySubmitResult.getPayDetailResultList().add(payDetailResult);
        log.info("[快捷支付]-[支付提交]-处理结束|本次处理结果-{}", payDetailResult);
    }
}
