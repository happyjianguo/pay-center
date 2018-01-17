package com.dream.pay.center.api.facade;

import com.dream.pay.bean.DataResult;
import com.dream.pay.center.api.request.RefundApplyRequest;
import com.dream.pay.center.api.request.RefundQueryRequest;
import com.dream.pay.center.api.response.RefundApplyResult;
import com.dream.pay.center.api.response.RefundQueryResult;

/**
 * 统一退款服务接口
 *
 * @author mengzhenbin
 */
public interface UnifiedRefundService {

    /**
     * 退款申请
     *
     * @param refundApplyRequest 退款申请请求
     * @return RefundApplyResult 退款申请结果
     */
    DataResult<RefundApplyResult> refundApply(RefundApplyRequest refundApplyRequest);

    /**
     * 退款查询
     *
     * @param refundQueryRequest 退款查询请求
     * @return RefundQueryResult 退款查询结果
     */
    DataResult<RefundQueryResult> refundQuery(RefundQueryRequest refundQueryRequest);

}
