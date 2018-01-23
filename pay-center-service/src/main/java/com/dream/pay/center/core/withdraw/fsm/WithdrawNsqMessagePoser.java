package com.dream.pay.center.core.withdraw.fsm;

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
        WithdrawNsqMessage withdrawNsqMessage = new WithdrawNsqMessage();
        UnifiedBizCode unifiedBizCode = BizCodeUtils.parse(fundsWithdrawDetailEntity.getWithdrawNote());
        withdrawNsqMessage.setBizProd(unifiedBizCode.getBizProdCode());
        withdrawNsqMessage.setBizMode(unifiedBizCode.getBizModeCode());
        withdrawNsqMessage.setBizAction(unifiedBizCode.getBizActionCode());
        withdrawNsqMessage.setBizSubAction(unifiedBizCode.getBizPayToolCode());
        withdrawNsqMessage.setWithdrawChannel(unifiedBizCode.getBizChannelCode());
        withdrawNsqMessage.setOutBizNo(fundsWithdrawDetailEntity.getBizTradeNo());
        withdrawNsqMessage.setAcquireNo(fundsWithdrawDetailEntity.getPayTradeNo());
        withdrawNsqMessage.setWithdrawNo(fundsWithdrawDetailEntity.getWithdrawDetailNo());
        withdrawNsqMessage.setMerchantNo(fundsWithdrawDetailEntity.getMerchantNo());
        withdrawNsqMessage.setPartnerId(fundsWithdrawDetailEntity.getPartnerId());
        withdrawNsqMessage.setWithdrawStatus(fundsWithdrawStatus.name());
        withdrawNsqMessage.setWithdrawAmount(fundsWithdrawDetailEntity.getWithdrawAmount());
        withdrawNsqMessage.setCurrency(fundsWithdrawDetailEntity.getCurrency());
        withdrawNsqMessage.setWithdrawCreateTime(fundsWithdrawDetailEntity.getCreateTime());
        withdrawNsqMessage.setWithdrawFinishTime(fundsWithdrawDetailEntity.getOutFinishTime());
        withdrawNsqMessage.setTradeDesc("提现");
        return withdrawNsqMessage;
    }
}
