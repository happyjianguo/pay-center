package com.dream.pay.center.dao;

import com.dream.pay.center.model.FundsPayDetailEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface FundsPayDetailDao {
    int deleteByPrimaryKey(Long id);

    int insert(FundsPayDetailEntity record);

    int insertSelective(FundsPayDetailEntity record);

    FundsPayDetailEntity selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(FundsPayDetailEntity record);

    int updateByPrimaryKeyWithBLOBs(FundsPayDetailEntity record);

    int updateByPrimaryKey(FundsPayDetailEntity record);

    FundsPayDetailEntity loadByDetailNo(@Param("payDetailNo") String payDetailNo);

    List<FundsPayDetailEntity> loadByItemNo(@Param("payTradeItemsNo") String payTradeItemsNo);

    List<FundsPayDetailEntity> loadForRefund(@Param("payTradeNo") String payTradeNo);
}