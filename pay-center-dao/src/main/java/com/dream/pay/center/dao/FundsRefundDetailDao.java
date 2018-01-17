package com.dream.pay.center.dao;

import com.dream.pay.center.model.FundsRefundDetailEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface FundsRefundDetailDao {
    int deleteByPrimaryKey(Long id);

    int insert(FundsRefundDetailEntity record);

    int insertSelective(FundsRefundDetailEntity record);

    FundsRefundDetailEntity selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(FundsRefundDetailEntity record);

    int updateByPrimaryKeyWithBLOBs(FundsRefundDetailEntity record);

    int updateByPrimaryKey(FundsRefundDetailEntity record);

    List<FundsRefundDetailEntity> loadByPayItemsNo(@Param("payTradeItemsNo") String payTradeItemsNo);
}