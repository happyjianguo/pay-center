package com.dream.pay.center.core.withdraw.fsm;

import com.dream.center.out.mock.dto.OperationStatusEnum;
import com.dream.pay.center.model.FundsWithdrawDetailEntity;
import com.dream.pay.center.model.FundsWithdrawJobEntity;
import com.dream.pay.center.status.FundsWithdrawStatus;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;


/**
 * 提现状态机入口
 *
 * @author 孟振滨
 * @since 2017-10-23
 */
@Setter
@Getter
public class WithdrawFSM {

    private Map<FundsWithdrawStatus, WithdrawStatusFlow> statusFlowMaps;

    public OperationStatusEnum handler(FundsWithdrawDetailEntity fundsWithdrawDetailEntity,
                                       FundsWithdrawJobEntity fundsWithdrawJobEntity) {
        FundsWithdrawStatus withdrawStatus = FundsWithdrawStatus.getByCode(fundsWithdrawDetailEntity.getWithdrawStatus());
        if (statusFlowMaps.containsKey(withdrawStatus)) {
            return statusFlowMaps.get(withdrawStatus)
                    .statusFlow(fundsWithdrawDetailEntity, fundsWithdrawJobEntity);
        }
        return OperationStatusEnum.UNKNOW;
    }

}
