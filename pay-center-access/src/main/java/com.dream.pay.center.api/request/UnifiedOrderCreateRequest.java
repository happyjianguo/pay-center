package com.dream.pay.center.api.request;

import com.dream.pay.enums.*;
import com.dream.pay.validators.InEnum;
import com.youzan.platform.util.json.JsonUtil;
import lombok.*;
import org.apache.commons.lang3.RandomStringUtils;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.io.IOException;

/**
 * 创建统一收单请求类:包含外部订单号、支付金额、收款方信息等属性
 *
 * @author mengzhenbin
 */
@Data
@Setter
@Getter
@ToString(callSuper = true)
public class UnifiedOrderCreateRequest extends AbstractBaseRequest {

    /**
     * 业务线-交易单号
     */
    @NotNull(message = "交易单号不能为空")
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
     * 交易金额
     */
    @Min(value = 0, message = "交易金额参数不能小于0")
    private long tradeAmount = 0;

    /**
     * 币种：目前默认为人民币
     */
    @NotNull(message = "币种参数不能为空")
    @InEnum(clazz = CurrencyCode.class, property = "num", message = "币种参数非法")
    private String currencyCode = CurrencyCode.CNY.getNum();


    /**
     * 业务标识
     *
     * @see BizProdEnum
     */
    @InEnum(clazz = BizProdEnum.class, property = "code", message = "业务标识参数非法")
    private int bizProd;

    /**
     * 业务模式
     *
     * @see BizModeEnum
     */
    @InEnum(clazz = BizModeEnum.class, property = "code", message = "业务模式参数非法")
    private int bizMode;

    /**
     * 业务操作类型
     *
     * @see BizActionEnum
     */
    @NotNull(message = "业务操作类型参数不能为空")
    @InEnum(clazz = BizActionEnum.class, property = "code", message = "业务操作类型参数非法")
    private int bizAction;

    /**
     * 交易描述
     */
    @NotNull(message = "交易描述不能为空")
    private String tradeDesc;

    /**
     * 买家账号
     */
    private String userNo;

    /**
     * 买家昵称
     */
    private String userName;

    /**
     * 交易超时时间，绝对时间,格式为yyyy-MM-dd HH:mm:ss,时区为GMT+08:00(东八区，北京时间)
     */
    private String expiredTime;


    /**
     * 扩展上下文字段
     */
    private String extendInfo;


    public static void main(String args[]) throws IOException {
        UnifiedOrderCreateRequest unifiedOrderCreateRequest = new UnifiedOrderCreateRequest();
        unifiedOrderCreateRequest.setBizTradeNo("123456789");
        unifiedOrderCreateRequest.setPartnerId(PartnerIdEnum.LINGSHOU.getCode());
        unifiedOrderCreateRequest.setMerchantNo("8888888888");
        unifiedOrderCreateRequest.setTradeAmount(1);
        unifiedOrderCreateRequest.setCurrencyCode("CNY");
        unifiedOrderCreateRequest.setBizProd(BizProdEnum.TRADING_TYPE_COMMON.getCode());
        unifiedOrderCreateRequest.setBizMode(BizModeEnum.TRADING_MODE_GUARANTEE.getCode());
        unifiedOrderCreateRequest.setBizAction(BizActionEnum.PAY.getCode());
        unifiedOrderCreateRequest.setTradeDesc("普通交易");
        unifiedOrderCreateRequest.setUserNo("6666666666");
        unifiedOrderCreateRequest.setUserName("钻石王老五");
        unifiedOrderCreateRequest.setExpiredTime("2017-12-12 12:23:34");
        unifiedOrderCreateRequest.setExtendInfo("扩展信息");
        unifiedOrderCreateRequest.setRequestId(RandomStringUtils.randomAlphabetic(24));
        System.out.print(JsonUtil.obj2json(unifiedOrderCreateRequest));
    }
}
