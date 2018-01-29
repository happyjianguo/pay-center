package com.dream.pay.center.core.refund.nsq;

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
        UnifiedBizCode unifiedBizCode = BizCodeUtils.parse(fundsRefundDetailEntity.getRefundNote());
        RefundNsqMessage refundNsqMessage = RefundNsqMessage.builder()
                .bizProd(unifiedBizCode.getBizProdCode())
                .bizMode(unifiedBizCode.getBizModeCode())
                .bizAction(unifiedBizCode.getBizActionCode())
                .bizSubAction(unifiedBizCode.getBizPayToolCode())
                .refundChannel(unifiedBizCode.getBizChannelCode())
                .outBizNo(fundsRefundDetailEntity.getBizTradeNo())
                .acquireNo(fundsRefundDetailEntity.getPayTradeNo())
                .refundDetailNo(fundsRefundDetailEntity.getRefundDetailNo())
                .refundStatus(fundsRefundStatus.name())
                .refundAmount(fundsRefundDetailEntity.getRefundAmount())
                .currency(fundsRefundDetailEntity.getCurrency())
                .refundCreateTime(fundsRefundDetailEntity.getCreateTime())
                .refundFinishTime(fundsRefundDetailEntity.getOutFinishTime())
                .tradeDesc("退款").build();
        return refundNsqMessage;
    }
}
