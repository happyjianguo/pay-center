package com.dream.pay.center.service.out;

import com.dream.center.out.mock.dto.AccountRefundOperationResult;
import com.dream.pay.center.model.FundsRefundDetailEntity;
import com.dream.pay.center.model.FundsTradeInfoEntity;
import com.dream.pay.center.model.FundsTradeItemsEntity;

import java.util.List;

/**
 * 调用清结算接口
 */
public interface SettlementService {

    /**
     * 退款预处理-冻结接口
     *
     * @param fundsTradeItemsEntity
     * @param fundsRefundDetailEntityList
     * @return OperationResult
     */
    AccountRefundOperationResult refundFreeze(FundsTradeItemsEntity fundsTradeItemsEntity, List<FundsRefundDetailEntity> fundsRefundDetailEntityList);

    /**
     * 退款失败处理-解冻接口
     *
     * @param fundsRefundDetailEntity
     * @return OperationResult
     */
    AccountRefundOperationResult refundUnFreeze(FundsRefundDetailEntity fundsRefundDetailEntity);
}
