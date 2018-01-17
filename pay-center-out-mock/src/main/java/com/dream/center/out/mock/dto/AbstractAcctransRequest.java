package com.dream.center.out.mock.dto;

import com.dream.pay.enums.CurrencyCode;
import com.dream.pay.validators.InEnum;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.Map;

/**
 * @Author mengzhenbin
 * @Since 2017/12/2
 */
@Setter
@Getter
public class AbstractAcctransRequest {

    /**
     * 唯一业务号;唯一业务号和系统名称做幂等(总长度不超过64字符);唯一业务号用来核对;
     */
    @NotNull(message = "clientId不能为空")
    private String clientId;

    /**
     * 系统名称,总长度不超过32字符
     */
    @NotNull(message = "系统名称")
    private String appName;

    /**
     * 交易金额
     */
    @Min(value = 1, message = "金额必须大于0")
    private long amount;

    /**
     * 币种
     */
    @NotNull(message = "币种不能为空")
    @InEnum(clazz = CurrencyCode.class, property = "num", message = "币种参数非法")
    private Integer currencyCode;

    /**
     * 交易码 {@link com.dream.center.out.mock.enums.TransCodeEnum},使用getCode()方法
     */
    @NotNull(message = "交易码不能为空")
    private Integer transCode;

    /**
     * 子交易码 {@link com.dream.center.out.mock.enums.TransSubCodeEnum},使用getCode()方法
     */
    @NotNull(message = "子交易码不能为空")
    private Integer subTransCode;

    /**
     * 订单号,没有经过订单系统,则传自己流水号,总长度不超过64字符
     */
    @NotNull(message = "业务线业务单号")
    private String bizOrderNo;

    /**
     * 收单号,如果没有经过收单系统,则传订单号,总长度不超过32字符
     */
    @NotNull(message = "支付收单号")
    private String payOrderNo;

    /**
     * 交易备注,例如商品名称等,最多256个字符
     */
    @NotNull(message = "交易备注不能为空")
    private String remark;

    /**
     * 扩展字段
     */
    private Map<String, Object> extraMap;

    /**
     * 操作员,默认为8888
     */
    private String operator = "8888";

    /**
     * 发送请求的时间，格式"yyyyMMddHHmmss"
     */
    @NotNull(message = "requestTime不能为空")
    private String requestTime;

    /**
     * 业务线id,例如美业,微商城等,总长度不超过32字符
     */
    @NotNull(message = "parternerId不能为空")
    private String partnerId;
}
