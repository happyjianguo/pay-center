package com.dream.pay.center.dao;

import com.dream.pay.center.model.FundsIdempotencyEntity;

public interface FundsIdempotencyDao {
    int deleteByPrimaryKey(Long id);

    int insert(FundsIdempotencyEntity record);

    int insertSelective(FundsIdempotencyEntity record);

    FundsIdempotencyEntity selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(FundsIdempotencyEntity record);

    int updateByPrimaryKey(FundsIdempotencyEntity record);
}