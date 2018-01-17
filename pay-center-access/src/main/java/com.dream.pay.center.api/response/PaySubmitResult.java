package com.dream.pay.center.api.response;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * 提交支付结果类
 *
 * @author mengzhenbin
 */
@Data
@Setter
@Getter
@ToString(callSuper = true)
public class PaySubmitResult extends BusinessBaseResult {

    /**
     * 支付号
     */
    private String payItemNo;

    /**
     * 实际支付金额==所有支付工具的支付总和
     */
    private long payAmount = 0;

    /**
     * 支付工具处理详情
     */
    private List<PayDetailResult> payDetailResultList;

}
