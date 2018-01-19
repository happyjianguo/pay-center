package com.dream.pay.center.service.out.impl;


import com.dream.center.out.mock.dto.*;
import com.dream.pay.center.model.FundsPayDetailEntity;
import com.dream.pay.center.model.FundsRefundDetailEntity;
import com.dream.pay.center.model.FundsTradeItemsEntity;
import com.dream.pay.center.model.FundsWithdrawDetailEntity;
import com.dream.pay.center.service.convert.ChannelDtoBuilder;
import com.dream.pay.center.service.out.ChannelService;
import com.dream.pay.channel.access.apply.GatePayApplyFacade;
import com.dream.pay.channel.access.apply.GateRefundApplyFacade;
import com.dream.pay.channel.access.apply.GateWithdrawApplyFacade;
import com.dream.pay.channel.access.dto.*;
import com.dream.pay.channel.access.enums.TradeStatus;
import com.dream.pay.channel.access.repair.GatePayQueryFacade;
import com.dream.pay.channel.access.repair.GateRefundQueryFacade;
import com.dream.pay.channel.access.repair.GateWithdrawQueryFacade;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 调用【渠道】接口
 */
@Slf4j
@Service
public class ChannelServiceImpl implements ChannelService {

    @Resource
    private GatePayApplyFacade gatePayApplyFacade;
    @Resource
    private GatePayQueryFacade gatePayQueryFacade;
    @Resource
    private GateRefundApplyFacade gateRefundApplyFacade;
    @Resource
    private GateRefundQueryFacade gateRefundQueryFacade;
    @Resource
    private GateWithdrawApplyFacade gateWithdrawApplyFacade;
    @Resource
    private GateWithdrawQueryFacade gateWithdrawQueryFacade;

    /**
     * 支付申请接口
     *
     * @param fundsPayDetailEntity
     * @return ChannelPayOperationResult
     */
    public PayOperationResult payApply(FundsPayDetailEntity fundsPayDetailEntity) {
        PayApplyReqDTO PayApplyReqDTO = null;
        try {
            PayApplyReqDTO = ChannelDtoBuilder.buildPayApplyReqDTO(fundsPayDetailEntity);
            log.info("调用渠道-支付申请-请求参数-[{}]", PayApplyReqDTO);

            PayApplyRepDTO payApplyRepDTO = gatePayApplyFacade.payApply(PayApplyReqDTO);

            log.info("调用渠道-支付申请-返回结果-[{}]", payApplyRepDTO);

            if (null == payApplyRepDTO || TradeStatus.FAIL.equals(payApplyRepDTO.getTradeStatus())) {
                return ChannelDtoBuilder.buildChannelPayFailResult();
            }
            return ChannelDtoBuilder.buildChanelPaySuccessResult(payApplyRepDTO);
        } catch (Exception e) {
            log.error("调用渠道-支付申请-发生异常-[{}]", PayApplyReqDTO, e);
            return ChannelDtoBuilder.buildChannelPayFailResult();
        }
    }

    /**
     * 支付查询接口
     *
     * @param fundsPayDetailEntity
     * @return ChannelPayOperationResult
     */
    public PayOperationResult payQuery(FundsPayDetailEntity fundsPayDetailEntity) {
        PayQueryReqDTO payQueryReqDTO = null;
        try {
            payQueryReqDTO = ChannelDtoBuilder.buildPayQueryReqDTO(fundsPayDetailEntity);
            log.info("调用渠道-支付查询-请求参数-[{}]", payQueryReqDTO);

            PayQueryRepDTO payQueryRepDTO = gatePayQueryFacade.payQuery(payQueryReqDTO);

            log.info("调用渠道-支付查询-返回结果-[{}]", payQueryRepDTO);

            if (null == payQueryRepDTO || TradeStatus.FAIL.equals(payQueryRepDTO.getTradeStatus())) {
                return ChannelDtoBuilder.buildChannelPayFailResult();
            }

            return ChannelDtoBuilder.buildChanelPaySuccessResult(payQueryRepDTO);
        } catch (Exception e) {
            log.error("调用渠道-支付查询-发生异常-[{}]", payQueryReqDTO, e);
            return ChannelDtoBuilder.buildChannelPayFailResult();
        }
    }


    /**
     * 退款申请接口
     *
     * @param fundsTradeItemsEntity
     * @param fundsRefundDetailEntity
     * @return ChannelRefundOperationResult
     */
    public RefundApplyRepDTO refundApply(FundsTradeItemsEntity fundsTradeItemsEntity, FundsRefundDetailEntity fundsRefundDetailEntity) {
        RefundApplyReqDTO refundApplyReqDTO = null;
        RefundApplyRepDTO refundApplyRepDTO = null;
        try {
            refundApplyReqDTO = ChannelDtoBuilder.buildRefundApplyReqDTO(fundsRefundDetailEntity);
            log.info("调用渠道-退款申请-请求参数-[{}]", refundApplyReqDTO);

            refundApplyRepDTO = gateRefundApplyFacade.refundApply(refundApplyReqDTO);

            log.info("调用渠道-退款申请-返回结果-[{}]", refundApplyRepDTO);

        } catch (Exception e) {
            log.error("调用渠道-退款申请-发生异常-[{}]-[{}]", refundApplyReqDTO, refundApplyRepDTO, e);
            refundApplyRepDTO = new RefundApplyRepDTO();
            refundApplyRepDTO.setSuccess(false);
        }
        return refundApplyRepDTO;
    }


    /**
     * 退款查询接口
     *
     * @param fundsRefundDetailEntity
     * @return ChannelRefundOperationResult
     */
    public RefundQueryRepDTO refundQuery(FundsRefundDetailEntity fundsRefundDetailEntity) {
        RefundQueryReqDTO refundQueryReqDTO = null;
        RefundQueryRepDTO refundQueryRepDTO = null;
        try {
            refundQueryReqDTO = ChannelDtoBuilder.buildRefundQueryReqDTO(fundsRefundDetailEntity);
            log.info("调用渠道-退款申请-请求参数-[{}]", refundQueryReqDTO);

            refundQueryRepDTO = gateRefundQueryFacade.refundQuery(refundQueryReqDTO);

            log.info("调用渠道-退款申请-返回结果-[{}]", refundQueryRepDTO);

        } catch (Exception e) {
            log.error("调用渠道-退款申请-发生异常-[{}]-[{}]", refundQueryReqDTO, refundQueryRepDTO, e);
            refundQueryRepDTO = new RefundQueryRepDTO();
            refundQueryRepDTO.setSuccess(false);
        }
        return refundQueryRepDTO;
    }

    /**
     * 提现申请接口
     *
     * @param fundsWithdrawDetailEntity
     * @return ChannelWithdrawOperationResult
     */
    public WithdrawApplyRepDTO withdrawApply(FundsWithdrawDetailEntity fundsWithdrawDetailEntity) {
        WithdrawApplyReqDTO withdrawApplyReqDTO = null;
        WithdrawApplyRepDTO withdrawApplyRepDTO = null;
        try {
            withdrawApplyReqDTO = ChannelDtoBuilder.buildWithdrawApplyReqDTO(fundsWithdrawDetailEntity);
            log.info("调用渠道-提现申请-请求参数-[{}]", withdrawApplyReqDTO);

            withdrawApplyRepDTO = gateWithdrawApplyFacade.withdrawApply(withdrawApplyReqDTO);

            log.info("调用渠道-提现申请-返回结果-[{}]", withdrawApplyRepDTO);

        } catch (Exception e) {
            log.error("调用渠道-退款申请-发生异常-[{}]-[{}]", withdrawApplyReqDTO, withdrawApplyRepDTO, e);
            withdrawApplyRepDTO = new WithdrawApplyRepDTO();
            withdrawApplyRepDTO.setSuccess(false);
        }
        return withdrawApplyRepDTO;
    }

    /**
     * 提现查询接口
     *
     * @param fundsWithdrawDetailEntity
     * @return ChannelWithdrawOperationResult
     */
    public WithdrawQueryRepDTO withdrawQuery(FundsWithdrawDetailEntity fundsWithdrawDetailEntity) {
        WithdrawQueryReqDTO withdrawQueryReqDTO = null;
        WithdrawQueryRepDTO withdrawQueryRepDTO = null;
        try {
            withdrawQueryReqDTO = new WithdrawQueryReqDTO();
            withdrawQueryReqDTO.setWithdrawNo(fundsWithdrawDetailEntity.getWithdrawDetailNo());

            log.info("调用渠道-提现查询-请求参数-[{}]", withdrawQueryReqDTO);

            withdrawQueryRepDTO = gateWithdrawQueryFacade.withdrawQuery(withdrawQueryReqDTO);

            log.info("调用渠道-提现查询-返回结果-[{}]", withdrawQueryRepDTO);

        } catch (Exception e) {
            log.error("调用渠道-提现查询-发生异常-[{}]-[{}]", withdrawQueryReqDTO, withdrawQueryRepDTO, e);
            withdrawQueryRepDTO = new WithdrawQueryRepDTO();
            withdrawQueryRepDTO.setSuccess(false);
        }
        return withdrawQueryRepDTO;
    }

}
