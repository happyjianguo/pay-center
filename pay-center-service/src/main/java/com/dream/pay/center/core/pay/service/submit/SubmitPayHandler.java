package com.dream.pay.center.core.pay.service.submit;

import com.dream.pay.center.api.response.PaySubmitResult;
import com.dream.pay.center.common.exception.BusinessException;
import com.dream.pay.center.service.context.FundsPayContext;

public interface SubmitPayHandler {

    void submitPay(FundsPayContext fundsPayContext, PaySubmitResult paySubmitResult) throws BusinessException;


}
