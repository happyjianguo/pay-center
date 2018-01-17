package com.dream.pay.center.api.facade;

import com.dream.pay.bean.DataResult;
import com.dream.pay.center.api.request.RechargeApplyRequest;
import com.dream.pay.center.api.response.RechargeApplyResult;

/**
 * 统一充值服务接口
 *
 * @author mengzhenbin
 */
public interface UnifiedRechargeService {

    /**
     * 充值服务：
     *
     * @param rechargeApplyRequest 充值请求
     * @return RechargeApplyResult 充值结果
     */
    DataResult<RechargeApplyResult> rechargeApply(RechargeApplyRequest rechargeApplyRequest);

}
