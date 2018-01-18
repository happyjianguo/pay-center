package com.dream.pay.center.core.pay.service.confirm;

import com.dream.center.out.mock.dto.PayOperationResult;
import com.dream.pay.center.common.exception.BusinessException;
import com.dream.pay.center.service.context.FundsPayContext;

public interface ConfirmPayHandler {

    void confirm(FundsPayContext fundsPayContext, PayOperationResult payOperationResult) throws BusinessException;
}
