package com.dream.pay.center.api.facade;

import com.dream.center.out.mock.dto.PayOperationResult;
import com.dream.pay.bean.DataResult;
import com.dream.pay.center.api.request.PaySubmitRequest;
import com.dream.pay.center.api.response.PaySubmitResult;

/**
 * 统一支付服务接口
 *
 * @author mengzhenbin
 */
public interface UnifiedPayService {

    /**
     * 提交支付：
     * <ul>
     * <li>渠道支付：根据收单创建支付单并调用支付渠道完成预支付</li>
     * <li>虚拟支付：根据收单创建支付单并调用支付工具完成支付</li>
     * <li>组合支付：根据收单创建支付单并调用支付渠道完成预支付</li>
     * <p>
     * <p>
     * </ul>
     *
     * @param paySubmitRequest 单次支付请求
     * @return 支付结果
     */
    DataResult<PaySubmitResult> paySubmit(PaySubmitRequest paySubmitRequest);


    /**
     * 支付通知
     *
     * @param payResult
     */
    void payNotify(PayOperationResult payResult);


}
