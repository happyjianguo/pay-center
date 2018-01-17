package com.dream.pay.center.component;

import com.dream.pay.center.common.enums.ErrorEnum;
import com.dream.pay.center.common.exception.ComponentException;
import com.dream.pay.center.dao.FundsIdempotencyDao;
import com.dream.pay.center.common.enums.OperatorEnum;
import com.dream.pay.center.model.FundsIdempotencyEntity;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;

/**
 * 幂等性组件实现类
 * <p>
 * Created by mengzhenbin on 16/6/27.
 */
@Slf4j
@Component
public class IdempotencyComponentImpl implements IdempotencyComponent {

    @Resource
    private FundsIdempotencyDao fundsIdempotencyDao;

    public void idempotency(String key, OperatorEnum orderTypeEnum) {
        //如果为空则不需要进行校验
        if (StringUtils.isBlank(key)) {
            return;
        }
        String ideKey =
                this.genKey(orderTypeEnum.getIdenpotencyKeyPreFix(), key);
        try {
            insertDB(ideKey);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("{},key={}", ErrorEnum.IDEMPOTENT_EXCEPTION.getDescription(), key, e);
            throw new ComponentException(ErrorEnum.IDEMPOTENT_EXCEPTION, "key=" + key);
        }
    }

    /**
     * 生成幂等性key
     */
    private String genKey(String prefix, String serialNo) {
        return prefix + "-" + serialNo;
    }

    /**
     * 插入数据库来验证幂等性
     */
    private void insertDB(String key) {
        FundsIdempotencyEntity fundsIdempotencyEntity = new FundsIdempotencyEntity();
        fundsIdempotencyEntity.setCreateTime(new Date());
        fundsIdempotencyEntity.setIdempotencyKey(key);
        fundsIdempotencyDao.insertSelective(fundsIdempotencyEntity);
    }
}
