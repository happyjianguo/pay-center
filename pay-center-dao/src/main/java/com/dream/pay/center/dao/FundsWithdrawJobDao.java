package com.dream.pay.center.dao;

import com.dream.pay.center.model.FundsWithdrawJobEntity;
import org.apache.ibatis.annotations.Param;

public interface FundsWithdrawJobDao {
    int deleteByPrimaryKey(Long id);

    int insert(FundsWithdrawJobEntity record);

    int insertSelective(FundsWithdrawJobEntity record);

    FundsWithdrawJobEntity selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(FundsWithdrawJobEntity record);

    int updateByPrimaryKey(FundsWithdrawJobEntity record);

    int updateTypeById(@Param("jobType") Integer type,
                       @Param("jobTypeDesc") String desc,
                       @Param("id") Long id);
}