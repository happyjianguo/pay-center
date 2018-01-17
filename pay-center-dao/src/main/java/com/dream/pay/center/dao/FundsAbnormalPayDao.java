package com.dream.pay.center.dao;

import com.dream.pay.center.model.FundsAbnormalPayEntity;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface FundsAbnormalPayDao {
    int deleteByPrimaryKey(Long id);

    int insert(FundsAbnormalPayEntity record);

    int insertSelective(FundsAbnormalPayEntity record);

    FundsAbnormalPayEntity selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(FundsAbnormalPayEntity record);

    int updateByPrimaryKey(FundsAbnormalPayEntity record);

    List<FundsAbnormalPayEntity> loadForRecovery(@Param("type") int type,
                                                 @Param("beginTime") Date gmtMinExecute,
                                                 @Param("endTime") Date gmtMaxExecute);
}