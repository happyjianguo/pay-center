package com.dream.pay.center.service.context;

import com.dream.center.out.mock.dto.OperationStatusEnum;
import com.dream.pay.bean.Money;
import com.dream.pay.center.common.enums.PayMode;
import com.dream.pay.center.common.enums.PayTagEnum;
import com.dream.pay.center.model.FundsPayDetailEntity;
import com.dream.pay.center.status.FundsTradeItemStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 支付统一上下文
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FundsPayContext extends FundsBaseContext {

    /**
     * 支付详情单
     */
    private List<FundsPayDetailEntity> fundsPayDetailEntitys;


    /**
     * 当前处理支付详情单
     */
    private FundsPayDetailEntity currentDetail;

    /**
     * 前置支付详情单处理结果
     *
     * @see OperationStatusEnum
     */
    private OperationStatusEnum preDetailStatus;

    /**
     * 支付模式
     *
     * @see PayMode
     */
    private PayMode payMode;

    /**
     * 支付标签
     *
     * @see PayTagEnum
     */
    private PayTagEnum payTag;

    /**
     * 实际支付金额
     */
    private Money realPayAmount = new Money(0);

    /**
     * 支付单最终状态
     *
     * @see FundsTradeItemStatus
     */
    private FundsTradeItemStatus tradeItemStatus;


}
