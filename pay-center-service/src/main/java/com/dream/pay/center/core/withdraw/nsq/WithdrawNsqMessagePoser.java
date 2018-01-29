package com.dream.pay.center.core.withdraw.nsq;

import com.dream.pay.center.api.message.WithdrawNsqMessage;
import com.dream.pay.center.component.NsqMessagePoser;
import com.dream.pay.center.model.FundsWithdrawDetailEntity;
import com.dream.pay.center.status.FundsWithdrawStatus;
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
public class WithdrawNsqMessagePoser extends NsqMessagePoser {

    @Value("${nsq.withdraw.messgae.producer.topic}")
    private String topic;

    /**
     * 发送消息
     */
    public void sendNsqMessage(FundsWithdrawDetailEntity fundsWithdrawDetailEntity, FundsWithdrawStatus fundsWithdrawStatus) {
        WithdrawNsqMessage withdrawNsqMessage = composeWithdrawMessage(fundsWithdrawDetailEntity, fundsWithdrawStatus);
        send(topic, fundsWithdrawDetailEntity.getPayTradeNo() + "-" + fundsWithdrawStatus.name(), JsonUtil.toJson(withdrawNsqMessage));

    }

    /**
     * 创建提现消息体
     *
     * @param fundsWithdrawStatus
     * @param fundsWithdrawDetailEntity
     * @return
     */
    private WithdrawNsqMessage composeWithdrawMessage(FundsWithdrawDetailEntity fundsWithdrawDetailEntity, FundsWithdrawStatus fundsWithdrawStatus) {
        UnifiedBizCode unifiedBizCode = BizCodeUtils.parse(fundsWithdrawDetailEntity.getWithdrawNote());
        WithdrawNsqMessage withdrawNsqMessage = WithdrawNsqMessage.builder()
                .bizProd(unifiedBizCode.getBizProdCode())
                .bizMode(unifiedBizCode.getBizModeCode())
                .bizAction(unifiedBizCode.getBizActionCode())
                .bizSubAction(unifiedBizCode.getBizPayToolCode())
                .withdrawChannel(unifiedBizCode.getBizChannelCode())
                .outBizNo(fundsWithdrawDetailEntity.getBizTradeNo())
                .acquireNo(fundsWithdrawDetailEntity.getPayTradeNo())
                .withdrawNo(fundsWithdrawDetailEntity.getWithdrawDetailNo())
                .merchantNo(fundsWithdrawDetailEntity.getMerchantNo())
                .partnerId(fundsWithdrawDetailEntity.getPartnerId())
                .withdrawStatus(fundsWithdrawStatus.name())
                .withdrawAmount(fundsWithdrawDetailEntity.getWithdrawAmount())
                .currency(fundsWithdrawDetailEntity.getCurrency())
                .withdrawCreateTime(fundsWithdrawDetailEntity.getCreateTime())
                .withdrawFinishTime(fundsWithdrawDetailEntity.getOutFinishTime())
                .tradeDesc("提现").build();
        return withdrawNsqMessage;
    }
}
