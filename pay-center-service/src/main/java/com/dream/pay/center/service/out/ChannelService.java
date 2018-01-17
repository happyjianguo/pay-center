package com.dream.pay.center.service.out;

import com.dream.center.out.mock.dto.ChannelRefundOperationResult;
import com.dream.center.out.mock.dto.ChannelWithdrawOperationResult;
import com.dream.center.out.mock.dto.PayOperationResult;
import com.dream.pay.center.model.FundsPayDetailEntity;
import com.dream.pay.center.model.FundsRefundDetailEntity;
import com.dream.pay.center.model.FundsTradeItemsEntity;
import com.dream.pay.center.model.FundsWithdrawDetailEntity;

/**
 * 调用渠道接口
 */
public interface ChannelService {


    /**
     * 支付申请接口
     *
     * @param fundsPayDetailEntity
     * @return OperationResult
     */
    PayOperationResult payApply(FundsPayDetailEntity fundsPayDetailEntity);

    /**
     * 支付查询接口
     *
     * @param fundsPayDetailEntity
     * @return ChannelWithdrawOperationResult
     */
    PayOperationResult payQuery(FundsPayDetailEntity fundsPayDetailEntity);


    /**
     * 退款申请接口
     *
     * @param fundsTradeItemsEntity
     * @param fundsRefundDetailEntity
     * @return ChannelWithdrawOperationResult
     */
    ChannelRefundOperationResult refundApply(FundsTradeItemsEntity fundsTradeItemsEntity, FundsRefundDetailEntity fundsRefundDetailEntity);

    /**
     * 提现查询接口
     *
     * @param fundsRefundDetailEntity
     * @return ChannelRefundOperationResult
     */
    ChannelRefundOperationResult refundQuery(FundsRefundDetailEntity fundsRefundDetailEntity);


    /**
     * 提现申请接口
     *
     * @param fundsWithdrawDetailEntity
     * @return ChannelWithdrawOperationResult
     */
    ChannelWithdrawOperationResult withdrawApply(FundsWithdrawDetailEntity fundsWithdrawDetailEntity);

    /**
     * 提现查询接口
     *
     * @param fundsWithdrawDetailEntity
     * @return ChannelWithdrawOperationResult
     */
    ChannelWithdrawOperationResult withdrawQuery(FundsWithdrawDetailEntity fundsWithdrawDetailEntity);

}
