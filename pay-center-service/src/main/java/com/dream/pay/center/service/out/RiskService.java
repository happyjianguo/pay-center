package com.dream.pay.center.service.out;


/**
 * 调用风控接口
 */
public interface RiskService {
    /**
     * 提现黑白名单检查接口
     *
     * @param merchantNo
     * @return boolean
     */
    public void riskCheck(String merchantNo);
}
