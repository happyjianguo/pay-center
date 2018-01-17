package com.dream.pay.center.refund.fsm;

import com.dream.center.out.mock.dto.OperationStatusEnum;
import com.dream.pay.center.model.FundsRefundDetailEntity;
import com.dream.pay.center.model.FundsRefundJobEntity;
import com.dream.pay.center.model.FundsTradeItemsEntity;
import com.dream.pay.center.status.FundsRefundStatus;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;


/**
 * 退款状态机入口
 *
 * @author 孟振滨
 * @since 2017-10-23
 */
@Setter
@Getter
public class RefundFSM {

    private Map<FundsRefundStatus, RefundStatusFlow> statusFlowMaps;

    public OperationStatusEnum handler(FundsTradeItemsEntity fundsTradeItemsEntity, FundsRefundDetailEntity fundsRefundDetailEntity,
                                       FundsRefundJobEntity fundsRefundJobEntity) {
        FundsRefundStatus refundStatus;
        if (null == fundsRefundDetailEntity || null == fundsRefundJobEntity) {
            refundStatus = FundsRefundStatus.APPLYING;
        } else {
            refundStatus = FundsRefundStatus.getByCode(fundsRefundDetailEntity.getRefundStatus());
        }
        if (statusFlowMaps.containsKey(refundStatus)) {
            return statusFlowMaps.get(refundStatus)
                    .statusFlow(fundsTradeItemsEntity, fundsRefundDetailEntity, fundsRefundJobEntity);
        }
        return OperationStatusEnum.UNKNOW;
    }

}
