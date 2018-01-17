package com.dream.pay.center.component;

import com.dream.pay.center.common.enums.PayTagEnum;
import com.dream.pay.center.dao.FundsAbnormalPayDao;
import com.dream.pay.center.model.FundsAbnormalPayEntity;
import com.dream.pay.center.model.FundsPayDetailEntity;
import com.dream.pay.center.status.JobStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.*;

/**
 * 异常支付处理组件实现类
 * <p>
 * Created by mengzhenbin on 16/6/27.
 */
@Slf4j
@Component
public class AbnormalPayComponentImpl implements AbnormalPayComponent {

    @Resource
    private FundsAbnormalPayDao fundsAbnormalPayDao;

    @Override
    public void save(FundsPayDetailEntity fundsPayDetailEntity, PayTagEnum payTag) {
        try {
            FundsAbnormalPayEntity abnormalPayDO = new FundsAbnormalPayEntity();
            abnormalPayDO.setBizTradeNo(fundsPayDetailEntity.getBizTradeNo());
            abnormalPayDO.setPayTradeNo(fundsPayDetailEntity.getPayTradeNo());
            abnormalPayDO.setPayTradeItemsNo(fundsPayDetailEntity.getPayTradeItemsNo());
            abnormalPayDO.setPayDetailNo(fundsPayDetailEntity.getPayDetailNo());
            abnormalPayDO.setStatus(JobStatus.TODO.getCode());
            abnormalPayDO.setType(payTag.getCode());
            Date now = new Date();
            abnormalPayDO.setCreateTime(now);
            abnormalPayDO.setUpdateTime(now);
            fundsAbnormalPayDao.insert(abnormalPayDO);
        } catch (Exception e) {
            log.error("异常支付单[{}],异常类型[{}],存储发生异常，", fundsPayDetailEntity, payTag, e);
        }

    }

    @Override
    public List<FundsAbnormalPayEntity> loadForRecovery(int type, Date gmtMinExecute, Date gmtMaxExecute) {
        return fundsAbnormalPayDao.loadForRecovery(type, gmtMinExecute, gmtMaxExecute);
    }

}

