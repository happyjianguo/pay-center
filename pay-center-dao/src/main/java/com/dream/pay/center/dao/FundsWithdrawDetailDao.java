package com.dream.pay.center.dao;

import com.dream.pay.center.model.FundsWithdrawDetailEntity;

public interface FundsWithdrawDetailDao {
    int deleteByPrimaryKey(Long id);

    int insert(FundsWithdrawDetailEntity record);

    int insertSelective(FundsWithdrawDetailEntity record);

    FundsWithdrawDetailEntity selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(FundsWithdrawDetailEntity record);

    int updateByPrimaryKeyWithBLOBs(FundsWithdrawDetailEntity record);

    int updateByPrimaryKey(FundsWithdrawDetailEntity record);

    FundsWithdrawDetailEntity loadBySelective(FundsWithdrawDetailEntity record);
}