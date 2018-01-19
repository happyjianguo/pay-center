package com.dream.pay.center.service.convert;

import com.dream.center.out.mock.dto.*;
import com.dream.pay.center.common.enums.ErrorEnum;
import com.dream.pay.center.model.FundsPayDetailEntity;
import com.dream.pay.center.model.FundsRefundDetailEntity;
import com.dream.pay.center.model.FundsTradeItemsEntity;
import com.dream.pay.center.model.FundsWithdrawDetailEntity;
import com.dream.pay.channel.access.dto.*;
import com.dream.pay.channel.access.enums.TradeType;
import com.dream.pay.enums.PartnerIdEnum;
import com.dream.pay.enums.PayTool;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @Author mengzhenbin
 * @Since 2018/1/18
 */
public class ChannelDtoBuilder {

    /**
     * 组装渠道支付申请参数
     *
     * @param fundsPayDetailEntity
     * @return
     */
    public static PayApplyReqDTO buildPayApplyReqDTO(FundsPayDetailEntity fundsPayDetailEntity) {
        PayApplyReqDTO payApplyReqDTO = new PayApplyReqDTO();
        payApplyReqDTO.setPartnerId(PartnerIdEnum.getByCode(fundsPayDetailEntity.getPartnerId()));
        payApplyReqDTO.setMerchantNo(fundsPayDetailEntity.getMerchantNo());
        payApplyReqDTO.setPayDetailNo(fundsPayDetailEntity.getPayDetailNo());
        payApplyReqDTO.setPayAmount(new BigDecimal(fundsPayDetailEntity.getPayAmount()));
        payApplyReqDTO.setPayType(PayTool.selectByName(fundsPayDetailEntity.getPayTool()));
        payApplyReqDTO.setTradeType(TradeType.PAY_APPLY);
        payApplyReqDTO.setReqDateTime(new Date());
        return payApplyReqDTO;
    }


    /**
     * 组装渠道支付查询参数
     *
     * @param fundsPayDetailEntity
     * @return
     */
    public static PayQueryReqDTO buildPayQueryReqDTO(FundsPayDetailEntity fundsPayDetailEntity) {
        PayQueryReqDTO payQueryReqDTO = new PayQueryReqDTO();
        payQueryReqDTO.setPartnerId(PartnerIdEnum.getByCode(fundsPayDetailEntity.getPartnerId()));
        payQueryReqDTO.setMerchantNo(fundsPayDetailEntity.getMerchantNo());
        payQueryReqDTO.setPayDetailNo(fundsPayDetailEntity.getPayDetailNo());
        payQueryReqDTO.setPayType(PayTool.selectByName(fundsPayDetailEntity.getPayTool()));
        payQueryReqDTO.setTradeType(TradeType.PAY_QUERY);
        payQueryReqDTO.setReqDateTime(new Date());
        return payQueryReqDTO;
    }

    /**
     * 组装退款申请参数
     *
     * @param fundsRefundDetailEntity
     * @return PayToolRefundApplyRequest
     */
    public static RefundApplyReqDTO buildRefundApplyReqDTO(FundsRefundDetailEntity fundsRefundDetailEntity) {
        RefundApplyReqDTO refundApplyReqDTO = new RefundApplyReqDTO();
        refundApplyReqDTO.setPayDetailNo(fundsRefundDetailEntity.getPayDetailNo());
        refundApplyReqDTO.setBankPayDetailNo(fundsRefundDetailEntity.getChannelReturnNo());
        refundApplyReqDTO.setRefundDetailNo(fundsRefundDetailEntity.getRefundDetailNo());
        refundApplyReqDTO.setRefundAmount(new BigDecimal(fundsRefundDetailEntity.getRefundAmount()));
        refundApplyReqDTO.setPartnerId(PartnerIdEnum.getByCode(fundsRefundDetailEntity.getMerchantNo()));
        refundApplyReqDTO.setMerchantNo(fundsRefundDetailEntity.getMerchantNo());
        refundApplyReqDTO.setPayType(PayTool.selectByName(fundsRefundDetailEntity.getPayTool()));
        return refundApplyReqDTO;
    }

    /**
     * 封装退款查询参数
     *
     * @param fundsRefundDetailEntity
     * @return PayToolRefundApplyRequest
     */
    public static RefundQueryReqDTO buildRefundQueryReqDTO(FundsRefundDetailEntity fundsRefundDetailEntity) {
        RefundQueryReqDTO refundQueryReqDTO = new RefundQueryReqDTO();
        refundQueryReqDTO.setPayDetailNo(fundsRefundDetailEntity.getPayDetailNo());
        refundQueryReqDTO.setBankPayDetailNo(fundsRefundDetailEntity.getChannelReturnNo());
        refundQueryReqDTO.setRefundDetailNo(fundsRefundDetailEntity.getRefundDetailNo());
        refundQueryReqDTO.setBankRefundDetailNo(fundsRefundDetailEntity.getChannelReturnNo());
        return refundQueryReqDTO;
    }

    /**
     * 组装提现申请参数
     *
     * @param fundsWithdrawDetailEntity
     * @return PayToolWithdrawApplyRequest
     */
    public static WithdrawApplyReqDTO buildWithdrawApplyReqDTO(FundsWithdrawDetailEntity fundsWithdrawDetailEntity) {
        WithdrawApplyReqDTO withdrawApplyReqDTO = new WithdrawApplyReqDTO();
        withdrawApplyReqDTO.setWithdrawNo(fundsWithdrawDetailEntity.getWithdrawDetailNo());
        withdrawApplyReqDTO.setMerchantNo(fundsWithdrawDetailEntity.getMerchantNo());
        withdrawApplyReqDTO.setWithdrawAmount(fundsWithdrawDetailEntity.getWithdrawAmount());
        withdrawApplyReqDTO.setRecvBankCode(fundsWithdrawDetailEntity.getInstId());
        withdrawApplyReqDTO.setRecvBankName(fundsWithdrawDetailEntity.getInstName());
        withdrawApplyReqDTO.setRecvAccType(fundsWithdrawDetailEntity.getInstAccountType());
        withdrawApplyReqDTO.setRecvAccNo(fundsWithdrawDetailEntity.getInstAccountNo());
        withdrawApplyReqDTO.setRecvAccName(fundsWithdrawDetailEntity.getInstAccountName());
        withdrawApplyReqDTO.setMemo("提现申请");
        return withdrawApplyReqDTO;
    }


    /**
     * 组装渠道支付处理成功返回结果
     *
     * @param payApplyRepDTO
     * @return
     */
    public static PayOperationResult buildChanelPaySuccessResult(PayApplyRepDTO payApplyRepDTO) {
        PayOperationResult payOperationResult = new PayOperationResult();

        payOperationResult.setSuccess(true);
        payOperationResult.setOperateResultCode(OperationStatusEnum.SUCCESS);
        payOperationResult.setErrorCode(payApplyRepDTO.getChlRtnCode());
        payOperationResult.setErrorMessage(payApplyRepDTO.getChlRtnMsg());

        payOperationResult.setPayTool(payApplyRepDTO.getPayType());
        payOperationResult.setBizChannel(payApplyRepDTO.getBizChannel());
        payOperationResult.setPayDetailNo(payApplyRepDTO.getPayDetailNo());
        payOperationResult.setPayChannelNo(payApplyRepDTO.getBankPayDetailNo());
        payOperationResult.setPayBankNo(payApplyRepDTO.getBankPayDetailNo());
        payOperationResult.setPaymentTime(payApplyRepDTO.getChlFinishTime());
        payOperationResult.setRealAmount(payApplyRepDTO.getPayAmount().longValue());
        payOperationResult.setRepContent(payApplyRepDTO.getRepContent());

        return payOperationResult;
    }


    /**
     * 组装渠道支付处理成功返回结果
     *
     * @param payQueryRepDTO
     * @return
     */
    public static PayOperationResult buildChanelPaySuccessResult(PayQueryRepDTO payQueryRepDTO) {
        PayOperationResult payOperationResult = new PayOperationResult();

        payOperationResult.setSuccess(true);
        payOperationResult.setOperateResultCode(OperationStatusEnum.SUCCESS);
        payOperationResult.setErrorCode(payQueryRepDTO.getChlRtnCode());
        payOperationResult.setErrorMessage(payQueryRepDTO.getChlRtnMsg());

        payOperationResult.setPayTool(payQueryRepDTO.getPayType());
        payOperationResult.setBizChannel(payQueryRepDTO.getBizChannel());
        payOperationResult.setPayDetailNo(payQueryRepDTO.getPayDetailNo());
        payOperationResult.setPayChannelNo(payQueryRepDTO.getBankPayDetailNo());
        payOperationResult.setPayBankNo(payQueryRepDTO.getBankPayDetailNo());
        payOperationResult.setPaymentTime(payQueryRepDTO.getChlFinishTime());
        payOperationResult.setRealAmount(payQueryRepDTO.getPayAmount().longValue());

        return payOperationResult;
    }


    /**
     * 封装渠道支付理失败返回结果
     *
     * @return channelRefundOperationResult
     */
    public static PayOperationResult buildChannelPayFailResult() {
        PayOperationResult payOperationResult = new PayOperationResult();
        payOperationResult.setSuccess(false);
        payOperationResult.setOperateResultCode(OperationStatusEnum.UNKNOW);
        payOperationResult.setErrorCode(ErrorEnum.UN_KNOW_EXCEPTION.getCode());
        payOperationResult.setErrorMessage(ErrorEnum.UN_KNOW_EXCEPTION.getDescription());
        return payOperationResult;
    }
}
