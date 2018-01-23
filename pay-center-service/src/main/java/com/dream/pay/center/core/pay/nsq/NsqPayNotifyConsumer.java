package com.dream.pay.center.core.pay.nsq;

import com.dream.center.out.mock.dto.OperationStatusEnum;
import com.dream.center.out.mock.dto.PayOperationResult;
import com.dream.pay.center.core.pay.handler.PayNotifyHandler;
import com.dream.pay.channel.access.dto.PayNotifyRepDTO;
import com.dream.pay.channel.access.enums.TradeStatus;
import com.dream.pay.nsq.Listener;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Resource;

/**
 * 支付成功消息-消费者
 */
@Slf4j
public class NsqPayNotifyConsumer extends Listener<PayNotifyRepDTO> {

    @Resource
    private PayNotifyHandler payNotifyHandler;


    @Override
    public void onEvent(PayNotifyRepDTO payNotifyRepDTO) {
        log.info("接受到支付成功消息,入参={}", payNotifyRepDTO);
        long startTime = System.currentTimeMillis();
        PayOperationResult payOperationResult = convert2PayOperationResult(payNotifyRepDTO);
        payNotifyHandler.handle(payOperationResult);
        log.info("接受到支付成功消息,结果={},耗时={}ms", payNotifyRepDTO, (System.currentTimeMillis() - startTime));

    }

    private PayOperationResult convert2PayOperationResult(PayNotifyRepDTO payNotifyRepDTO) {
        PayOperationResult payOperationResult = new PayOperationResult();

        payOperationResult.setPayTool(payNotifyRepDTO.getPayType());
        payOperationResult.setBizChannel(payNotifyRepDTO.getBizChannel());
        payOperationResult.setPayDetailNo(payNotifyRepDTO.getPayDetailNo());
        payOperationResult.setPayChannelNo(payNotifyRepDTO.getBankPayDetailNo());
        payOperationResult.setPayBankNo(payNotifyRepDTO.getBankPayDetailNo());
        payOperationResult.setPaymentTime(payNotifyRepDTO.getBankFinishTime());
        payOperationResult.setRealAmount(payNotifyRepDTO.getPayAmount().longValue());
        payOperationResult.setSuccess(true);
        payOperationResult.setOperateResultCode(convertOperationStatus(payNotifyRepDTO.getTradeStatus()));
        payOperationResult.setErrorCode(payNotifyRepDTO.getChlRtnCode());
        payOperationResult.setErrorMessage(payNotifyRepDTO.getChlRtnMsg());

        return payOperationResult;
    }

    private OperationStatusEnum convertOperationStatus(TradeStatus tradeStatus) {
        switch (tradeStatus) {
            case FAIL:
                return OperationStatusEnum.FAIL;
            case PROCESS:
                return OperationStatusEnum.PROCESSING;
            case UNKNOW:
                return OperationStatusEnum.UNKNOW;
            case SUCCESS:
                return OperationStatusEnum.SUCCESS;
            default:
                return OperationStatusEnum.PROCESSING;
        }
    }

}