package com.dream.pay.center.dao;

import com.dream.pay.center.model.FundsTradeInfoEntity;
import org.apache.ibatis.annotations.Param;

public interface FundsTradeInfoDao {
    int deleteByPrimaryKey(Integer id);

    int insert(FundsTradeInfoEntity record);

    int insertSelective(FundsTradeInfoEntity record);

    FundsTradeInfoEntity selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(FundsTradeInfoEntity record);

    int updateByPrimaryKey(FundsTradeInfoEntity record);

    FundsTradeInfoEntity selectByBizNoAndMerchantNo(@Param("bizTradeNo") String bizTradeNo,
                                                    @Param("merchantNo") String merchantNo);

    int updateStatusByPayTradeNo(@Param("status") Integer status,
                                 @Param("payTradeNo") String payTradeNo);

    FundsTradeInfoEntity selectByPayTradeNoForUpdate(@Param("payTradeNo") String payTradeNo);

    void incRefundInProcessAmount(@Param("state") Integer state,
                                  @Param("payTradeNo") String payTradeNo,
                                  @Param("amount") Long amount);

    void decRefundInProcessAmount(@Param("payTradeNo") String payTradeNo,
                                  @Param("amount") Long amount);

    void updateRefundTradeInfo(@Param("state") int state,
                               @Param("amount") Long refundAmount,
                               @Param("payTradeNo") String payTradeNo);

    FundsTradeInfoEntity loadByPayTradeNo(@Param("payTradeNo") String payTradeNo);
}