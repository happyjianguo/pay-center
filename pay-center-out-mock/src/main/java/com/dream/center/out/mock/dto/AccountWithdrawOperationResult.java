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
public class AccountWithdrawOperationResult extends OperationBaseResult {
    /**
     * 提现记账凭证单号
     */
    private String withdrawVoucherNo;

    /**
     * 提现账务操作时间
     */
    private Date accountingTime;

}
