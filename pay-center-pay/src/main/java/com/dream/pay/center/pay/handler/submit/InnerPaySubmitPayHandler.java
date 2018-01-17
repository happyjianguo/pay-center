package com.dream.pay.center.pay.handler.submit;

import com.dream.center.out.mock.dto.OperationStatusEnum;
import com.dream.pay.center.api.response.PayDetailResult;
import com.dream.pay.center.api.response.PaySubmitResult;
import com.dream.pay.center.common.enums.PayMode;
import com.dream.pay.center.common.enums.PayToolType;
import com.dream.pay.center.common.exception.BusinessException;
import com.dream.pay.center.model.FundsPayDetailEntity;
import com.dream.pay.center.pay.service.PayCoreService;
import com.dream.pay.center.service.context.FundsPayContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component("innerPaySubmitPayHandler")
@Slf4j
public class InnerPaySubmitPayHandler implements SubmitPayHandler {

    @Resource
    private PayCoreService payCoreService;

    public void submitPay(FundsPayContext fundsPayContext, PaySubmitResult paySubmitResult) throws BusinessException {
        FundsPayDetailEntity currentDetail = fundsPayContext.getCurrentDetail();
        log.info("[虚拟账户支付]-[支付提交]-处理开始|本次处理支付单-{}", currentDetail);

        PayDetailResult payDetailResult = new PayDetailResult();

        payDetailResult.setPayAmount(currentDetail.getPayAmount());
        payDetailResult.setPayDetailNo(currentDetail.getPayDetailNo());
        payDetailResult.setPayTool(currentDetail.getPayTool());
        payDetailResult.setPayToolType(PayToolType.toPayToolType(currentDetail.getPayToolType()));

        //如果驱动单为虚拟账户支付单，则设置前置处理单为调用成功并且调用支付确认组件进行处理
        if (PayMode.INNER_PAY_8.equals(fundsPayContext.getPayMode()) || PayMode.INNER_PAY_24.equals(fundsPayContext.getPayMode())) {
            fundsPayContext.setPreDetailStatus(OperationStatusEnum.SUCCESS);
            payCoreService.confirmPay(fundsPayContext,null);//调用支付确认组件
        }

        payDetailResult.setPayStatus(String.valueOf(currentDetail.getPayStatus()));
        paySubmitResult.getPayDetailResultList().add(payDetailResult);
        log.info("[虚拟账户支付]-[支付提交]-处理结束|本次处理结果-{}", payDetailResult);
    }
}
