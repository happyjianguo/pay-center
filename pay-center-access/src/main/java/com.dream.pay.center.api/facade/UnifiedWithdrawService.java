package com.dream.pay.center.api.facade;

import com.dream.pay.bean.DataResult;
import com.dream.pay.center.api.request.*;
import com.dream.pay.center.api.response.WithdrawApplyResult;
import com.dream.pay.center.api.response.WithdrawQueryResult;

/**
 * 统一提现服务接口
 *
 * @author mengzhenbin
 */
public interface UnifiedWithdrawService {

    /**
     * 提现申请
     *
     * @param withdrawApplyRequest 提现申请请求
     * @return WithdrawApplyResult 退款申请结果
     */
    DataResult<WithdrawApplyResult> withdrawApply(WithdrawApplyRequest withdrawApplyRequest);

    /**
     * 提现查询
     *
     * @param withdrawQueryRequest 提现查询请求
     * @return WithdrawQueryResult 提现查询结果
     */
    DataResult<WithdrawQueryResult> withdrawQuery(WithdrawQueryRequest withdrawQueryRequest);

}
