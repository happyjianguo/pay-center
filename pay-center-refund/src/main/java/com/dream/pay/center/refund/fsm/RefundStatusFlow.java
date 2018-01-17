package com.dream.pay.center.refund.fsm;

import com.dream.center.out.mock.dto.OperationStatusEnum;
import com.dream.pay.center.model.FundsRefundDetailEntity;
import com.dream.pay.center.model.FundsRefundJobEntity;
import com.dream.pay.center.model.FundsTradeItemsEntity;

/**
 * 退款状态机流转接口
 *
 * @author 孟振滨
 * @since 2017-12-02
 */
public interface RefundStatusFlow {
    OperationStatusEnum statusFlow(FundsTradeItemsEntity fundsTradeItemsEntity,
                                   FundsRefundDetailEntity fundsRefundDetailEntity,
                                   FundsRefundJobEntity fundsRefundJobEntity);
}
