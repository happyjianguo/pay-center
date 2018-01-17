package com.dream.center.out.mock.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

/**
 * 账务提现接口请求类
 *
 * @Author mengzhenbin
 * @Since 2017/12/2
 */
@Setter
@Getter
public class AcctransWithdrawRequest extends AbstractAcctransRequest{

    @NotNull(message = "商户号必须填写")
    private String merchantNo;
    @NotNull(message = "账户类型必须填写")
    private Integer accountType;
}
