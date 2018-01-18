package com.dream.pay.center.core.pay.service.confirm;

import com.dream.pay.center.model.FundsPayDetailEntity;
import com.dream.pay.center.model.FundsTradeInfoEntity;
import com.dream.pay.constants.UnifiedBizCode;
import com.dream.pay.enums.BizActionEnum;
import com.dream.pay.enums.PayTool;

/**
 * @Author mengzhenbin
 * @Since 2018/1/12
 */
public class AbstractPayConfirmHandler {

    /**
     * 封装四码一号
     *
     * @param fundsTradeInfoEntity
     * @param fundsPayDetailEntity
     * @return
     */
    public String buildPayNote(FundsTradeInfoEntity fundsTradeInfoEntity, FundsPayDetailEntity fundsPayDetailEntity) {
        UnifiedBizCode unifiedBizCode = new UnifiedBizCode();
        unifiedBizCode.setBizProdCode(String.valueOf(fundsTradeInfoEntity.getBizProd()));//产品码
        unifiedBizCode.setBizModeCode(String.valueOf(fundsTradeInfoEntity.getBizMode()));//业务模式码
        unifiedBizCode.setBizActionCode(String.valueOf(BizActionEnum.PAY.getCode()));//业务流向码
        unifiedBizCode.setBizPayToolCode(String.valueOf(PayTool.selectByName(fundsPayDetailEntity.getPayTool()).getBizSubAction()));//业务流向子码
        unifiedBizCode.setBizChannelCode(String.valueOf(fundsPayDetailEntity.getBizChannel()));//渠道号
        return unifiedBizCode.getFullCode();
    }
}
