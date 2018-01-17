package com.dream.pay.center.dao;

import com.dream.pay.center.model.FundsRefundJobEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface FundsRefundJobDao {
    int deleteByPrimaryKey(Long id);

    int insert(FundsRefundJobEntity record);

    int insertSelective(FundsRefundJobEntity record);

    FundsRefundJobEntity selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(FundsRefundJobEntity record);

    int updateByPrimaryKey(FundsRefundJobEntity record);

    int updateTypeById(@Param("jobType") Integer type,
                       @Param("jobTypeDesc") String desc,
                       @Param("id") Long id);

    List<FundsRefundJobEntity> loadByPayItemsNo(@Param("payTradeItemsNo") String payTradeItemsNo);
}