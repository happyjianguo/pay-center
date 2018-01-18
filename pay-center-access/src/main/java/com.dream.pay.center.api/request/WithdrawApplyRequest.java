package com.dream.pay.center.api.request;

import com.dream.pay.enums.PartnerIdEnum;
import com.dream.pay.enums.BizProdEnum;
import com.dream.pay.enums.CurrencyCode;
import com.dream.pay.validators.InEnum;
import com.youzan.platform.util.json.JsonUtil;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.commons.lang3.RandomStringUtils;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;
import java.io.IOException;

/**
 * 提现申请参数类
 *
 * @author mengzhenbin
 */
@Data
@Setter
@Getter
@ToString(callSuper = true)
public class WithdrawApplyRequest extends AbstractBaseRequest {

    /**
     * 调用方保证唯一，主要用来防止请求重发
     */
    @NotBlank(message = "外部提现单号不能为空")
    private String outBizNo;

    /**
     * 产品码
     * 1001：普通交易，余额提现
     * 1003：储值卡，储值卡提现
     */
    @InEnum(clazz = BizProdEnum.class, property = "code", message = "提现产品码不合法")
    private int bizProd;

    /**
     * 合作方ID：在商户平台注册后，分配的一个商户ID
     **/
    @NotBlank(message = "合作方ID不能为空")
    private String partnerId;


    /**
     * 付款方帐号,传卖家的商户号
     **/
    @NotBlank(message = "付款方帐号不能为空")
    @Pattern(regexp = "[0-9]{1,19}", message = "付款方帐号非法")
    private String merchantNo;

    /**
     * 商户平台的绑卡id
     */
    @NotBlank(message = "提现卡id不能为空")
    private String cardBindId;

    /**
     * 提现金额
     */
    @Min(value = 1, message = "提现金额参数不能小于0")
    @Max(value = 5000000, message = "提现金额参数不能大于5万")
    private long withdrawAmount;

    /**
     * 币种：目前默认为人民币
     */
    @NotBlank(message = "币种参数不能为空")
    @InEnum(clazz = CurrencyCode.class, property = "num", message = "币种参数非法")
    private String currencyCode = CurrencyCode.CNY.getNum();

    /**
     * 备注字段
     */
    private String memo = "提现申请";

    /**
     * 扩展字段，用于后续其他操作
     */
    @Length(max = 100, message = "extInfo参数不能超过100")
    private String extInfo;

    public static void main(String args[]) throws IOException {
        WithdrawApplyRequest withdrawApplyRequest = new WithdrawApplyRequest();
        withdrawApplyRequest.setOutBizNo("123456789");
        withdrawApplyRequest.setBizProd(BizProdEnum.TRADING_TYPE_COMMON.getCode());
        withdrawApplyRequest.setPartnerId(PartnerIdEnum.MEIYE.getCode());
        withdrawApplyRequest.setMerchantNo("8888888888");
        withdrawApplyRequest.setCardBindId("1234");
        withdrawApplyRequest.setWithdrawAmount(1);
        withdrawApplyRequest.setCurrencyCode(CurrencyCode.CNY.getNum());
        withdrawApplyRequest.setMemo("提现申请");
        withdrawApplyRequest.setExtInfo("");
        withdrawApplyRequest.setRequestId(RandomStringUtils.randomAlphabetic(24));
        System.out.print(JsonUtil.obj2json(withdrawApplyRequest));
    }
}
