package com.dream.pay.center.api.facade;

import com.dream.pay.bean.DataResult;
import com.dream.pay.center.api.request.UnifiedOrderCreateRequest;
import com.dream.pay.center.api.request.UnifiedOrderQueryRequest;
import com.dream.pay.center.api.response.UnifiedOrderCreateResult;
import com.dream.pay.center.api.response.UnifiedOrderQueryResult;

/**
 * 统一收单服务接口。
 *
 * @author mengzhenbin
 */
public interface UnifiedOrderService {

    /**
     * 创建收单功能：
     * <ul>
     * <li>一笔订单创建一笔收单</li>
     * <li>如果订单对应的收单已存在，会返回已存在收单标识</li>
     * <p>
     * </ul>
     *
     * @param request 创建统一收单请求
     * @return 统一收单结果
     */
    DataResult<UnifiedOrderCreateResult> create(UnifiedOrderCreateRequest request);

    /**
     * 查询收单功能：
     * <ul>
     * <li>查询收单状态</li>
     * </ul>
     *
     * @param request 查询统一收单请求
     * @return 统一收单结果
     */
    DataResult<UnifiedOrderQueryResult> query(UnifiedOrderQueryRequest request);
}
