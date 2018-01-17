package com.dream.pay.center.refund.fsm;

import com.dream.center.out.mock.dto.OperationStatusEnum;
import com.dream.pay.center.model.*;

/**
 * 退款申请失败==无操作
 *
 * @author 孟振滨
 * @since 2017-11-17
 */
public class RefundApplyFailState implements RefundStatusFlow {

    @Override
    public OperationStatusEnum statusFlow(FundsTradeItemsEntity fundsTradeItemsEntity, FundsRefundDetailEntity fundsRefundDetailEntity, FundsRefundJobEntity fundsRefundJobEntity) {
        return OperationStatusEnum.SUCCESS;
    }
}
