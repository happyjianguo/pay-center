package com.dream.pay.center.service.out;


import com.dream.center.out.mock.dto.PayOperationResult;
import com.dream.pay.center.model.FundsPayDetailEntity;

/**
 * 调用营销活动接口
 */
public interface ActiveService {
    /**
     * 营销活动支付
     *
     * @param payDetailEntity
     * @return AccountPayOperationResult
     */
    PayOperationResult activePay(FundsPayDetailEntity payDetailEntity);

}
