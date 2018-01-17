package com.dream.pay.center.api.response;

import com.dream.pay.center.api.enums.WithdrawResultEnum;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 * 提现申请结果类
 *
 * @author mengzhenbin
 */
@Data
@Setter
@Getter
public class WithdrawApplyResult extends BusinessBaseResult {

    /**
     * 提现状态
     */
    private WithdrawResultEnum withdrawStatus = WithdrawResultEnum.WITHDRAW_PROCESSING;

    /**
     * 是否幂等
     */
    private boolean isIdempotent = false;


}
