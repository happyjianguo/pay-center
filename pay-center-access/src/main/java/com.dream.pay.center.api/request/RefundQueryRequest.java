package com.dream.pay.center.api.request;


import com.dream.pay.bean.ValidationResult;
import com.dream.pay.center.common.enums.ErrorEnum;
import com.dream.pay.center.common.exception.IllegalArgsException;
import com.dream.pay.utils.ValidationUtils;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 退款查询参数类
 *
 * @author mengzhenbin
 */
@Data
@Setter
@Getter
@ToString(callSuper = true)
public class RefundQueryRequest extends AbstractBaseRequest {

    /**
     * 退款的原支付订单号
     */
    private String payTradeNo;

    /**
     * 退款申请时使用的业务线退款单号
     */
    private String outBizNo;
}
