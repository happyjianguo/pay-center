package com.dream.pay.center.component;


import com.dream.pay.center.common.enums.OperatorEnum;

/**
 * 幂等性组件
 * <p>
 * Created by mengzhenbin on 16/6/27.
 */
public interface IdempotencyComponent {

    /**
     * 幂等性控制
     *
     * @param key          幂等性key
     * @param operatorEnum 收单类型枚举
     */
    void idempotency(String key, OperatorEnum operatorEnum);
}
