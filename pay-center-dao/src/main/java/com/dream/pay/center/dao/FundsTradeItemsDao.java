package com.dream.pay.center.dao;

import com.dream.pay.center.model.FundsTradeItemsEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface FundsTradeItemsDao {
    int deleteByPrimaryKey(Integer id);

    int insert(FundsTradeItemsEntity record);

    int insertSelective(FundsTradeItemsEntity record);

    FundsTradeItemsEntity selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(FundsTradeItemsEntity record);

    int updateByPrimaryKey(FundsTradeItemsEntity record);

    FundsTradeItemsEntity loadByItemNo(@Param("payTradeItemsNo") String payTradeItemsNo);

    List<FundsTradeItemsEntity> loadByPayTradeNo(@Param("payTradeNo") String payTradeNo);

    void updateStatusByTradeItemNo(@Param("status") int status,
                                   @Param("payTradeItemsNo") String payTradeItemsNo);

    FundsTradeItemsEntity loadByBizTradeNo(@Param("bizTradeNo")String bizTradeNo,
                                           @Param("payTradeNo")String payTradeNo);
}

