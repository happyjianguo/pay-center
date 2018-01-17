package com.dream.pay.center.service.out.impl;


import com.dream.center.out.mock.dto.*;
import com.dream.center.out.mock.facade.ChannelPayService;
import com.dream.center.out.mock.facade.ChannelRefundService;
import com.dream.center.out.mock.facade.ChannelWithdrawService;
import com.dream.pay.center.common.enums.ErrorEnum;
import com.dream.pay.center.common.enums.PayTool;
import com.dream.pay.center.common.enums.PayToolType;
import com.dream.pay.center.model.FundsPayDetailEntity;
import com.dream.pay.center.model.FundsRefundDetailEntity;
import com.dream.pay.center.model.FundsTradeItemsEntity;
import com.dream.pay.center.model.FundsWithdrawDetailEntity;
import com.dream.pay.center.service.out.ChannelService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 调用【渠道】接口
 */
@Slf4j
@Service
public class ChannelServiceImpl implements ChannelService {

    /**
     * 支付申请接口
     *
     * @param fundsPayDetailEntity
     * @return ChannelPayOperationResult
     */
    public PayOperationResult payApply(FundsPayDetailEntity fundsPayDetailEntity) {
        PayToolPayApplyRequest payTooPayApplyRequest = null;
        try {
            payTooPayApplyRequest = buildPayToolPayApplyRequest(fundsPayDetailEntity);
            log.info("调用渠道-支付申请-请求参数-[{}]", payTooPayApplyRequest);
            PayOperationResult result = ChannelPayService.payApply(payTooPayApplyRequest);
            log.info("调用渠道-支付申请-返回结果-[{}]", result);
            return result;
        } catch (Exception e) {
            log.error("调用渠道-支付申请-发生异常-[{}]", payTooPayApplyRequest, e);
            return buildChannelPayFailResult();
        }
    }


    /**
     * 支付查询接口
     *
     * @param fundsPayDetailEntity
     * @return ChannelPayOperationResult
     */
    public PayOperationResult payQuery(FundsPayDetailEntity fundsPayDetailEntity) {
        PayToolPayQueryRequest payToolPayQueryRequest = null;
        try {
            payToolPayQueryRequest = new PayToolPayQueryRequest();
            payToolPayQueryRequest.setPayToolType(PayToolType.toPayToolType(fundsPayDetailEntity.getPayToolType()));
            payToolPayQueryRequest.setPayTool(PayTool.select(fundsPayDetailEntity.getPayTool()));
            payToolPayQueryRequest.setPayDetailNo(payToolPayQueryRequest.getPayDetailNo());
            payToolPayQueryRequest.setPayTradeItemNo(fundsPayDetailEntity.getPayTradeItemsNo());
            payToolPayQueryRequest.setPayTradeNo(fundsPayDetailEntity.getPayTradeNo());
            log.info("调用渠道-支付查询-请求参数-[{}]", payToolPayQueryRequest);
            PayOperationResult result = ChannelPayService.payQuery(payToolPayQueryRequest);
            log.info("调用渠道-支付查询-返回结果-[{}]", result);
            return result;
        } catch (Exception e) {
            log.error("调用渠道-支付查询-发生异常-[{}]", payToolPayQueryRequest, e);
            return buildChannelPayFailResult();
        }
    }

    /**
     * 退款申请接口
     *
     * @param fundsTradeItemsEntity
     * @param fundsRefundDetailEntity
     * @return ChannelRefundOperationResult
     */
    public ChannelRefundOperationResult refundApply(FundsTradeItemsEntity fundsTradeItemsEntity, FundsRefundDetailEntity fundsRefundDetailEntity) {
        PayToolRefundApplyRequest payToolRefundApplyRequest = null;
        try {
            payToolRefundApplyRequest = buildPayToolRefundApplyRequest(fundsTradeItemsEntity, fundsRefundDetailEntity);
            log.info("调用渠道-退款申请-请求参数-[{}]", payToolRefundApplyRequest);
            ChannelRefundOperationResult result = ChannelRefundService.refundApply(payToolRefundApplyRequest);
            log.info("调用渠道-退款申请-返回结果-[{}]", result);
            return result;
        } catch (Exception e) {
            log.error("调用渠道-退款申请-发生异常-[{}]", payToolRefundApplyRequest, e);
            return buildChannelRefundFailResult();
        }
    }


    /**
     * 退款查询接口
     *
     * @param fundsRefundDetailEntity
     * @return ChannelRefundOperationResult
     */
    public ChannelRefundOperationResult refundQuery(FundsRefundDetailEntity fundsRefundDetailEntity) {
        PayToolRefundQueryRequest payToolRefundQueryRequest = null;
        try {
            payToolRefundQueryRequest = new PayToolRefundQueryRequest();
            payToolRefundQueryRequest.setPayDetailNo(fundsRefundDetailEntity.getPayDetailNo());
            payToolRefundQueryRequest.setPayChannelDetailNo(fundsRefundDetailEntity.getChannelPayDetailNo());
            payToolRefundQueryRequest.setRefundDetailNo(fundsRefundDetailEntity.getRefundDetailNo());
            payToolRefundQueryRequest.setRefundChannelDetailNo(fundsRefundDetailEntity.getChannelReturnNo());
            log.info("调用渠道-退款申请-请求参数-[{}]", payToolRefundQueryRequest);
            ChannelRefundOperationResult result = ChannelRefundService.refundQuery(payToolRefundQueryRequest);
            log.info("调用渠道-退款申请-返回结果-[{}]", result);
            return result;
        } catch (Exception e) {
            log.error("调用渠道-退款申请-发生异常-[{}]", payToolRefundQueryRequest, e);
            return buildChannelRefundFailResult();
        }
    }

    /**
     * 提现申请接口
     *
     * @param fundsWithdrawDetailEntity
     * @return ChannelWithdrawOperationResult
     */
    public ChannelWithdrawOperationResult withdrawApply(FundsWithdrawDetailEntity fundsWithdrawDetailEntity) {
        PayToolWithdrawApplyRequest payToolWithdrawApplyRequest = null;
        try {
            payToolWithdrawApplyRequest = buildPayToolWithdrawApplyRequest(fundsWithdrawDetailEntity);
            log.info("调用渠道-提现申请-请求参数-[{}]", payToolWithdrawApplyRequest);
            ChannelWithdrawOperationResult result = ChannelWithdrawService.withdrawApply(payToolWithdrawApplyRequest);
            log.info("调用渠道-提现申请-返回结果-[{}]", result);
            return result;
        } catch (Exception e) {
            log.error("调用渠道-退款申请-发生异常-[{}]", payToolWithdrawApplyRequest, e);
            return buildChannelWithdrawFailResult();
        }
    }

    /**
     * 提现查询接口
     *
     * @param fundsWithdrawDetailEntity
     * @return ChannelWithdrawOperationResult
     */
    public ChannelWithdrawOperationResult withdrawQuery(FundsWithdrawDetailEntity fundsWithdrawDetailEntity) {
        PayToolWithdrawQueryRequest payToolWithdrawQueryRequest = null;
        try {
            payToolWithdrawQueryRequest = new PayToolWithdrawQueryRequest();
            payToolWithdrawQueryRequest.setWithdrawNo(fundsWithdrawDetailEntity.getWithdrawDetailNo());
            log.info("调用渠道-提现查询-请求参数-[{}]", payToolWithdrawQueryRequest);
            ChannelWithdrawOperationResult result = ChannelWithdrawService.withdrawQuery(payToolWithdrawQueryRequest);
            log.info("调用渠道-提现查询-返回结果-[{}]", result);
            return result;
        } catch (Exception e) {
            log.error("调用渠道-提现查询-发生异常-[{}]", payToolWithdrawQueryRequest, e);
            return buildChannelWithdrawFailResult();
        }
    }

    /**
     * 封装提现申请参数
     *
     * @param fundsWithdrawDetailEntity
     * @return PayToolWithdrawApplyRequest
     */
    private PayToolWithdrawApplyRequest buildPayToolWithdrawApplyRequest(FundsWithdrawDetailEntity fundsWithdrawDetailEntity) {
        PayToolWithdrawApplyRequest payToolWithdrawApplyRequest = new PayToolWithdrawApplyRequest();
        payToolWithdrawApplyRequest.setWithdrawNo(fundsWithdrawDetailEntity.getWithdrawDetailNo());
        payToolWithdrawApplyRequest.setMerchantNo(fundsWithdrawDetailEntity.getMerchantNo());
        payToolWithdrawApplyRequest.setWithdrawAmount(fundsWithdrawDetailEntity.getWithdrawAmount());
        payToolWithdrawApplyRequest.setRecvBankCode(fundsWithdrawDetailEntity.getInstId());
        payToolWithdrawApplyRequest.setRecvBankName(fundsWithdrawDetailEntity.getInstName());
        payToolWithdrawApplyRequest.setRecvAccType(fundsWithdrawDetailEntity.getInstAccountType());
        payToolWithdrawApplyRequest.setRecvAccNo(fundsWithdrawDetailEntity.getInstAccountNo());
        payToolWithdrawApplyRequest.setRecvAccName(fundsWithdrawDetailEntity.getInstAccountName());
        payToolWithdrawApplyRequest.setMemo("提现申请");
        return payToolWithdrawApplyRequest;
    }

    /**
     * 封装退款申请参数
     *
     * @param fundsRefundDetailEntity
     * @return PayToolRefundApplyRequest
     */
    private PayToolRefundApplyRequest buildPayToolRefundApplyRequest(FundsTradeItemsEntity fundsTradeItemsEntity, FundsRefundDetailEntity fundsRefundDetailEntity) {
        PayToolRefundApplyRequest payToolRefundApplyRequest = new PayToolRefundApplyRequest();

        payToolRefundApplyRequest.setPayDetailNo(fundsRefundDetailEntity.getPayDetailNo());
        payToolRefundApplyRequest.setPayChannelDetailNo(fundsRefundDetailEntity.getChannelPayDetailNo());
        payToolRefundApplyRequest.setRefundDetailNo(fundsRefundDetailEntity.getRefundDetailNo());

        //payToolRefundApplyRequest.setTotalAmount();//TODO
        payToolRefundApplyRequest.setRefundAmount(fundsRefundDetailEntity.getRefundAmount());

        payToolRefundApplyRequest.setPayToolType(PayTool.select(fundsRefundDetailEntity.getPayTool()).getPayToolType());
        payToolRefundApplyRequest.setPayTool(PayTool.select(fundsRefundDetailEntity.getPayTool()));
        return payToolRefundApplyRequest;
    }

    /**
     * 封装支付申请参数
     *
     * @param fundsPayDetailEntity
     * @return PayToolRefundApplyRequest
     */
    private PayToolPayApplyRequest buildPayToolPayApplyRequest(FundsPayDetailEntity fundsPayDetailEntity) {
        PayToolPayApplyRequest payToolPayApplyRequest = new PayToolPayApplyRequest();
        payToolPayApplyRequest.setOutBizNo(fundsPayDetailEntity.getBizTradeNo());
        payToolPayApplyRequest.setPayTradeNo(fundsPayDetailEntity.getPayTradeNo());
        payToolPayApplyRequest.setPayTradeItemNo(fundsPayDetailEntity.getPayTradeItemsNo());
        payToolPayApplyRequest.setPayDetailNo(fundsPayDetailEntity.getPayDetailNo());
        payToolPayApplyRequest.setTotalAmount(fundsPayDetailEntity.getPayAmount());
        payToolPayApplyRequest.setGoodDesc("普通交易");//TODO
        payToolPayApplyRequest.setPartnerId("");//TODO
        payToolPayApplyRequest.setMerchantNo(fundsPayDetailEntity.getMerchantNo());
        payToolPayApplyRequest.setUserNo(fundsPayDetailEntity.getUserNo());
        payToolPayApplyRequest.setPayToolType(PayToolType.toPayToolType(fundsPayDetailEntity.getPayToolType()));
        payToolPayApplyRequest.setPayTool(PayTool.select(fundsPayDetailEntity.getPayTool()));
        return payToolPayApplyRequest;
    }

    /**
     * 封装渠道支付处理失败返回结果
     *
     * @return channelRefundOperationResult
     */
    public PayOperationResult buildChannelPayFailResult() {
        PayOperationResult payOperationResult = new PayOperationResult();
        payOperationResult.setSuccess(false);
        payOperationResult.setOperateResultCode(OperationStatusEnum.UNKNOW);
        payOperationResult.setErrorCode(ErrorEnum.UN_KNOW_EXCEPTION.getCode());
        payOperationResult.setErrorMessage(ErrorEnum.UN_KNOW_EXCEPTION.getDescription());
        return payOperationResult;
    }

    /**
     * 封装渠道退款处理失败返回结果
     *
     * @return channelRefundOperationResult
     */
    public ChannelRefundOperationResult buildChannelRefundFailResult() {
        ChannelRefundOperationResult channelRefundOperationResult = new ChannelRefundOperationResult();
        channelRefundOperationResult.setSuccess(false);
        channelRefundOperationResult.setOperateResultCode(OperationStatusEnum.UNKNOW);
        channelRefundOperationResult.setErrorCode(ErrorEnum.UN_KNOW_EXCEPTION.getCode());
        channelRefundOperationResult.setErrorMessage(ErrorEnum.UN_KNOW_EXCEPTION.getDescription());
        return channelRefundOperationResult;
    }

    /**
     * 封装渠道提现处理失败返回结果
     *
     * @return channelRefundOperationResult
     */
    public ChannelWithdrawOperationResult buildChannelWithdrawFailResult() {
        ChannelWithdrawOperationResult channelWithdrawOperationResult = new ChannelWithdrawOperationResult();
        channelWithdrawOperationResult.setSuccess(false);
        channelWithdrawOperationResult.setOperateResultCode(OperationStatusEnum.UNKNOW);
        channelWithdrawOperationResult.setErrorCode(ErrorEnum.UN_KNOW_EXCEPTION.getCode());
        channelWithdrawOperationResult.setErrorMessage(ErrorEnum.UN_KNOW_EXCEPTION.getDescription());
        return channelWithdrawOperationResult;
    }

}
