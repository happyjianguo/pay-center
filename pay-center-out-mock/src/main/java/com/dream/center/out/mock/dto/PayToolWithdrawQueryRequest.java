package com.dream.center.out.mock.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotNull;

/**
 * 提现查询请求参数
 *
 * @author mengzhenbin
 * @version 1.0 on 2017/3/16
 */
@Data
@Setter
@Getter
@ToString(callSuper = true)
public class PayToolWithdrawQueryRequest extends PayToolBaseRequest {
    /**
     * 提现流水
     */
    @NotNull(message = "提现流水号不可为空")
    private String withdrawNo;
}
