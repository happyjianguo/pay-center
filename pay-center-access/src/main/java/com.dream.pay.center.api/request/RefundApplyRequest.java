package com.dream.pay.center.api.request;

import com.dream.pay.center.common.core.Money;
import com.dream.pay.center.common.enums.RefundMode;
import com.dream.pay.enums.CurrencyCode;
import com.dream.pay.utils.JsonUtil;
import com.dream.pay.validators.InEnum;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;

/**
 * 退款申请参数类
 *
 * @author mengzhenbin
 */
@Data
@Setter
@Getter
@ToString(callSuper = true)
public class RefundApplyRequest extends AbstractBaseRequest {

    /**
     * 需要退款的支付单号
     */
    @NotBlank(message = "原支付收单号不能为空")
    private String payTradeNo;

    /**
     * 调用方保证唯一，主要用来防止请求重发
     */
    @NotBlank(message = "外部退款单号不能为空")
    private String outBizNo;

    /**
     * 需要退款的金额
     */
    @NotNull(message = "退款金额不能为空")
    private long refundAmount;

    /**
     * 币种：目前默认为人民币
     */
    @NotBlank(message = "币种参数不能为空")
    @InEnum(clazz = CurrencyCode.class, property = "num", message = "币种参数非法")
    private String currencyCode = CurrencyCode.CNY.getNum();

    /**
     * 退款模式
     */
    @InEnum(clazz = RefundMode.class, property = "key", message = "退款模式非法")
    private Integer refundMode = RefundMode.ORIGINAL.getKey();

    /**
     * 备注字段
     */
    private String memo = "退款申请";

    /**
     * 扩展字段，用于后续其他操作
     */
    @Length(max = 100, message = "extInfo参数不能超过100")
    private String extInfo;

    public static void main(String args[]) {
        RefundApplyRequest refundApplyRequest = new RefundApplyRequest();
        refundApplyRequest.setPayTradeNo("556423676593045235");
        refundApplyRequest.setOutBizNo("5634372647938289473209429");
        refundApplyRequest.setRefundMode(RefundMode.ORIGINAL.getKey());
        refundApplyRequest.setRefundAmount(100);
        refundApplyRequest.setMemo("退款");
        System.out.print(JsonUtil.toJson(refundApplyRequest));
    }
}

