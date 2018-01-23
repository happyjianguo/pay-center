package com.dream.pay.center.core.refund.fsm;

import com.dream.pay.center.api.message.RefundNsqMessage;
import com.dream.pay.center.component.NsqMessagePoser;
import com.dream.pay.center.model.FundsRefundDetailEntity;
import com.dream.pay.center.status.FundsRefundStatus;
import com.dream.pay.enums.UnifiedBizCode;
import com.dream.pay.utils.BizCodeUtils;
import com.dream.pay.utils.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;

/**
 * @Author mengzhenbin
 * @Since 2018/1/10
 */
@Slf4j
public class RefundNsqMessagePoser extends NsqMessagePoser {

    @Value("${nsq.refund.messgae.producer.topic}")
    private String topic;

    /**
     * 发送消息
     */
    public void sendNsqMessage(FundsRefundDetailEntity fundsRefundDetailEntity, FundsRefundStatus fundsRefundStatus) {
        RefundNsqMessage refundNsqMessage = composeRefundNsqMessage(fundsRefundDetailEntity, fundsRefundStatus);
        send(topic, fundsRefundDetailEntity.getRefundDetailNo() + "-" + fundsRefundStatus.name(), JsonUtil.toJson(refundNsqMessage));

    }

    /**
     * 创建退款消息体
     *
     * @param fundsRefundStatus
     * @param fundsRefundDetailEntity
     * @return
     */
    private RefundNsqMessage composeRefundNsqMessage(FundsRefundDetailEntity fundsRefundDetailEntity, FundsRefundStatus fundsRefundStatus) {
        RefundNsqMessage refundNsqMessage = new RefundNsqMessage();
        UnifiedBizCode unifiedBizCode = BizCodeUtils.parse(fundsRefundDetailEntity.getRefundNote());
        refundNsqMessage.setBizProd(unifiedBizCode.getBizProdCode());
        refundNsqMessage.setBizMode(unifiedBizCode.getBizModeCode());
        refundNsqMessage.setBizAction(unifiedBizCode.getBizActionCode());
        refundNsqMessage.setBizSubAction(unifiedBizCode.getBizPayToolCode());
        refundNsqMessage.setRefundChannel(unifiedBizCode.getBizChannelCode());
        refundNsqMessage.setOutBizNo(fundsRefundDetailEntity.getBizTradeNo());
        refundNsqMessage.setAcquireNo(fundsRefundDetailEntity.getPayTradeNo());
        refundNsqMessage.setRefundDetailNo(fundsRefundDetailEntity.getRefundDetailNo());
        refundNsqMessage.setRefundStatus(fundsRefundStatus.name());
        refundNsqMessage.setRefundAmount(fundsRefundDetailEntity.getRefundAmount());
        refundNsqMessage.setCurrency(fundsRefundDetailEntity.getCurrency());
        refundNsqMessage.setRefundCreateTime(fundsRefundDetailEntity.getCreateTime());
        refundNsqMessage.setRefundFinishTime(fundsRefundDetailEntity.getOutFinishTime());
        refundNsqMessage.setTradeDesc("提现");
        return refundNsqMessage;
    }
}
