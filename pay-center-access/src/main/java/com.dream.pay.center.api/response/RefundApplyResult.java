package com.dream.pay.center.api.response;

import com.dream.pay.center.api.enums.RefundResultEnum;
import com.dream.pay.center.api.enums.WithdrawResultEnum;
import com.dream.pay.center.common.enums.ErrorEnum;
import lombok.*;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * 退款申请结果类
 *
 * @author mengzhenbin
 */
@Data
@Setter
@Getter
public class RefundApplyResult extends BusinessBaseResult {


    /**
     * 退款状态
     */
    private RefundResultEnum refundStatus = RefundResultEnum.REFUND_UN_KNOW;

    /**
     * 是否幂等
     */
    private boolean isIdempotent = false;


    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }


}
