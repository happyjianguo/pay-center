/**
 * Youzan.com Inc.
 * Copyright (c) 2012-2016 All Rights Reserved.
 */
package com.dream.pay.center.withdraw.enums;

/**
 * 提现异常处理类型
 *
 * @author mengzhenbin
 */
public enum WithdrawAutoRepairTypeEnum {
    //打回提现操作
    REJECT_WITHDRAW,
    //重试提现申请
    REDO_WITHDRAW_APPLY,
    //重试提现查询
    REDO_WITHDRAW_QUERY;

}
