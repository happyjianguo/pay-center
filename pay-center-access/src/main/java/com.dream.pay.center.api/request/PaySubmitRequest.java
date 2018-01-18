package com.dream.pay.center.api.request;

import com.dream.pay.enums.CurrencyCode;
import com.dream.pay.enums.PartnerIdEnum;
import com.dream.pay.enums.PayTool;
import com.dream.pay.enums.PayToolType;
import com.dream.pay.validators.InEnum;
import com.youzan.platform.util.json.JsonUtil;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 提交支付参数类
 *
 * @author mengzhenbin
 */
@Data
@Setter
@Getter
@ToString(callSuper = true)
public class PaySubmitRequest extends AbstractBaseRequest {

    /**
     * 收单-交易单号
     **/
    private String payTradeNo;

    /**
     * 业务线-交易单号
     */
    private String bizTradeNo;

    /**
     * 合作方ID：在商户平台注册后，分配的一个商户ID
     **/
    @NotNull(message = "合作方ID不能为空")
    private String partnerId;

    /**
     * 收款方帐号,卖家的商户号
     */
    @NotNull(message = "收款方帐号不能为空")
    @Pattern(regexp = "[0-9]{1,19}", message = "收款方帐号非法")
    private String merchantNo;

    /**
     * 支付金额：单位为币种最小单位，如人民币是分。
     */
    @Min(value = 0, message = "支付金额不能小于0")
    private long payAmount = 0;

    /**
     * 币种：目前默认为人民币
     */
    @NotNull(message = "币种不能为空")
    @InEnum(clazz = com.dream.pay.enums.CurrencyCode.class, property = "num", message = "币种参数非法")
    private String currencyCode = CurrencyCode.CNY.getNum();

    /**
     * 买家账号
     */
    @NotNull(message = "买家账号不能为空")
    private String userNo;

    /**
     * 买家支付密码--虚拟账户支付时用于安全校验
     */
    private String password;


    /**
     * 支付来源：PC／MOB／WAP
     */
    private String paySource;

    /**
     * 支付模式：所有支付工具类型的和
     * 例如：
     * 快捷支付：1
     * 快捷支付+余额支付：1+8=9
     * 快捷支付+余额支付+活动支付：1+8+16=25
     *
     * @see com.dream.pay.center.common.enums.PayMode
     */
    private Integer payMode;


    /**
     * 支付工具详情
     */
    List<PayDetailRequest> payDetailInfoList;


    public static void main(String args[]) throws IOException {
        PaySubmitRequest paySubmitRequest = new PaySubmitRequest();
        paySubmitRequest.setPayTradeNo("139089033102174112");
        paySubmitRequest.setBizTradeNo("123456789");
        paySubmitRequest.setPartnerId(PartnerIdEnum.LINGSHOU.getCode());
        paySubmitRequest.setMerchantNo("8888888888");
        paySubmitRequest.setPayAmount(1);
        paySubmitRequest.setCurrencyCode(CurrencyCode.CNY.getNum());
        paySubmitRequest.setUserNo("6666666666");
        paySubmitRequest.setPaySource("MOB");
        paySubmitRequest.setPayMode(4);
        List<PayDetailRequest> payDetailInfoList = new ArrayList<PayDetailRequest>();
        PayDetailRequest payDetailRequest = new PayDetailRequest();
        payDetailRequest.setPayToolType(PayToolType.PLAT_PAY);
        payDetailRequest.setPayTool(PayTool.ALIPAY_APP);
        payDetailRequest.setCurrencyCode(CurrencyCode.CNY.getNum());
        payDetailRequest.setPayAmount(1);
        payDetailRequest.setMemo("支付宝APP支付");
        payDetailInfoList.add(payDetailRequest);

        PayDetailRequest payDetailRequest2 = new PayDetailRequest();
        payDetailRequest2.setPayToolType(PayToolType.INNER_PAY);
        payDetailRequest2.setPayTool(PayTool.BALANCE);
        payDetailRequest2.setCurrencyCode(CurrencyCode.CNY.getNum());
        payDetailRequest2.setPayAmount(2);
        payDetailRequest2.setMemo("余额");
        payDetailInfoList.add(payDetailRequest2);


        PayDetailRequest payDetailRequest3 = new PayDetailRequest();
        payDetailRequest3.setPayToolType(PayToolType.ACTIVE_PAY);
        payDetailRequest3.setPayTool(PayTool.LI_JIAN);
        payDetailRequest3.setActivityId(666L);
        payDetailRequest3.setCurrencyCode(CurrencyCode.CNY.getNum());
        payDetailRequest3.setPayAmount(2);
        payDetailRequest3.setMemo("立减活动支付");
        payDetailInfoList.add(payDetailRequest3);

        paySubmitRequest.setPayDetailInfoList(payDetailInfoList);
        System.out.print(JsonUtil.obj2json(paySubmitRequest));
    }
}
