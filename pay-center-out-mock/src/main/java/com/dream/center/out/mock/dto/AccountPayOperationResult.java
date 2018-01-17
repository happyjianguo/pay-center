package com.dream.center.out.mock.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

/**
 * @Author mengzhenbin
 * @Since 2017/12/2
 */
@Setter
@Getter
@ToString(callSuper = true)
public class AccountPayOperationResult extends OperationBaseResult {
    /**
     * 余额支付记账凭证单号
     */
    private String payVoucherNo;

    /**
     * 余额账务操作时间
     */
    private Date accountingTime;

}
