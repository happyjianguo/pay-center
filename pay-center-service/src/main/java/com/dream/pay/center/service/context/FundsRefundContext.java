package com.dream.pay.center.service.context;

import com.dream.pay.center.model.FundsRefundDetailEntity;
import com.dream.pay.center.model.FundsRefundJobEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 退款统一上下文
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FundsRefundContext extends FundsBaseContext {

    /**
     * 退款数据
     */
    private List<FundsRefundDetailEntity> fundsRefundDetails;

    /**
     * 退款任务
     */
    private List<FundsRefundJobEntity> fundsRefundJobs;


}
