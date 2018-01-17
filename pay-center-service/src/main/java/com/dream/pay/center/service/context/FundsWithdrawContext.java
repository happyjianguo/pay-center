package com.dream.pay.center.service.context;

import com.dream.pay.center.model.FundsWithdrawDetailEntity;
import com.dream.pay.center.model.FundsWithdrawJobEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 提现统一上下文
 * <p>
 * Created by mengzhenbin on 16/7/21.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FundsWithdrawContext extends FundsBaseContext {

    /**
     * 提现数据
     */
    private FundsWithdrawDetailEntity fundsWithdrawDetailEntity;

    /**
     * 提现任务
     */
    private FundsWithdrawJobEntity fundsWithdrawJobEntity;


}
