package com.dream.pay.center.service.out;

import com.dream.center.out.mock.dto.BankCardInfoResult;

/**
 * 调用商户平台接口
 */
public interface MerchantService {
    public BankCardInfoResult queryCardInfo(String merchantNo, String userNo, int code);
}
