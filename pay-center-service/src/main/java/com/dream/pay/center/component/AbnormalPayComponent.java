package com.dream.pay.center.component;


import com.dream.pay.center.common.enums.PayTagEnum;
import com.dream.pay.center.model.FundsAbnormalPayEntity;
import com.dream.pay.center.model.FundsPayDetailEntity;

import java.util.Date;
import java.util.List;

/**
 * 异常支付处理组件
 * <p>
 * Created by mengzhenbin on 16/6/27.
 */
public interface AbnormalPayComponent {

    void save(FundsPayDetailEntity fundsPayDetailEntity, PayTagEnum payTag);

    List<FundsAbnormalPayEntity> loadForRecovery(int type, Date gmtMinExecute, Date gmtMaxExecute);

}
