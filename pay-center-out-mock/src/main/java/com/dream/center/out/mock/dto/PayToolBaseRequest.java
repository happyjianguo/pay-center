package com.dream.center.out.mock.dto;

import com.dream.pay.center.common.enums.PayTool;
import com.dream.pay.center.common.enums.PayToolType;
import lombok.Getter;
import lombok.Setter;

/**
 * 支付工具基础参数
 *
 * @Author mengzhenbin
 * @Since 2017/12/15
 */
@Setter
@Getter
public class PayToolBaseRequest {
    /**
     * 支付工具类型
     */
    private PayToolType payToolType;
    /**
     * 支付工具
     */
    private PayTool payTool;
}
