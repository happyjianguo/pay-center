package com.dream.center.out.mock.dto;

import com.dream.pay.enums.BizChannelEnum;
import com.dream.pay.enums.PayTool;
import com.dream.pay.utils.JsonUtil;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author mengzhenbin
 * @Since 2017/12/2
 */
@Setter
@Getter
@ToString(callSuper = true)
public class PayOperationResult extends OperationBaseResult {

    /**
     * 支付方式
     */
    @NotNull(message = "支付方式不能为空")
    private PayTool payTool;

    /**
     * 支付渠道码
     */
    @NotNull(message = "支付渠道码不能为空")
    private BizChannelEnum bizChannel;

    /**
     * 支付明细号
     */
    @NotNull(message = "支付明细号不能为空")
    private String payDetailNo;

    /**
     * 渠道返回的渠道流水号
     */
    private String payChannelNo;

    /**
     * 渠道返回的第三方流水号
     */
    private String payBankNo;

    /**
     * 渠道完成时间
     */
    private Date paymentTime;

    /**
     * 账务返回的记账流水号
     */
    private String payVoucherNo;

    /**
     * 账务完成时间
     */
    private Date accountingTime;

    /**
     * 实际支付金额
     */
    private long realAmount;

    /**
     * 业务上下文
     */
    private Map<String, String> bizContext = new HashMap<>();

    /**
     * 三方返回值,存放三方返回的一些数值,有收单和支付工具约定如何存放.
     * 目前存放deepLink值,不需要入库
     */
    private Map<String, Object> threePartyReturnValue = new HashMap<>();

    /**
     * 渠道报文
     */
    private String repContent;


    public static void main(String args[]) {
        PayOperationResult operationResult = new PayOperationResult();
        operationResult.setPayTool(PayTool.ALIPAY_APP);
        operationResult.setBizChannel(BizChannelEnum.ALIPAY);
        operationResult.setPayDetailNo("553028535955291780");
        operationResult.setPayChannelNo("23862746326436274632972");
        operationResult.setPayBankNo("3378436764297291931208320");
        operationResult.setPaymentTime(new Date());
        operationResult.setRealAmount(10000L);

        operationResult.setSuccess(Boolean.TRUE);
        operationResult.setOperateResultCode(OperationStatusEnum.SUCCESS);
        operationResult.setErrorCode("200");
        operationResult.setErrorMessage("处理成功");
        System.out.print(JsonUtil.toJson(operationResult));
    }

}
