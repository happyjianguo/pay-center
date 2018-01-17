package com.dream.pay.center.api.facade;

import com.dream.pay.bean.DataResult;
import com.dream.pay.center.api.request.TransferApplyRequest;
import com.dream.pay.center.api.response.TransferApplyResult;

/**
 * 统一转账服务接口
 *
 * @author mengzhenbin
 */
public interface UnifiedTransferService {

    /**
     * 转账申请
     *
     * @param transferApplyRequest 转账申请请求
     * @return TransferApplyResult 转账申请结果
     */
    DataResult<TransferApplyResult> transferApply(TransferApplyRequest transferApplyRequest);

}
