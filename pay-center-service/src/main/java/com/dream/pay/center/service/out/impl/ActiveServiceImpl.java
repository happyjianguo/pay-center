package com.dream.pay.center.service.out.impl;

import com.dream.center.out.mock.dto.*;
import com.dream.center.out.mock.enums.TransCodeEnum;
import com.dream.center.out.mock.enums.TransSubCodeEnum;
import com.dream.center.out.mock.facade.AcctransPayService;
import com.dream.center.out.mock.facade.AcctransWithdrawService;
import com.dream.center.out.mock.facade.ActivePayService;
import com.dream.center.out.mock.facade.ChannelPayService;
import com.dream.pay.center.common.enums.ErrorEnum;
import com.dream.pay.center.model.FundsPayDetailEntity;
import com.dream.pay.center.model.FundsWithdrawDetailEntity;
import com.dream.pay.center.service.out.AccountService;
import com.dream.pay.center.service.out.ActiveService;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.utils.DateUtils;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * 调用【营销活动】接口
 *
 * @author mengzhenbin
 * @since 2017-11-17
 */
@Slf4j
@Service
public class ActiveServiceImpl implements ActiveService {

    @Override
    public PayOperationResult activePay(FundsPayDetailEntity payDetailEntity) {
        AcctransPayRequest acctransPayRequest = null;
        try {
            acctransPayRequest = buildActivePayRequest(payDetailEntity);
            log.info("调用营销活动-支付申请-请求参数-[{}]", acctransPayRequest);
            PayOperationResult result = ActivePayService.activePay(acctransPayRequest);
            log.info("调用营销活动-支付申请-返回结果-[{}]", result);
            return result;
        } catch (Exception e) {
            log.error("调用营销活动-支付申请-发生异常-[{}]", acctransPayRequest, e);
            return buildActivePayFailResult();
        }
    }


    /**
     * 封装营销活动操作接口参数
     *
     * @param payDetailEntity
     * @return AcctransPayRequest
     */
    private AcctransPayRequest buildActivePayRequest(FundsPayDetailEntity payDetailEntity) {
        AcctransPayRequest accctransPayRequest = new AcctransPayRequest();
        accctransPayRequest.setAppName("收单-活动支付");
        accctransPayRequest.setClientId(payDetailEntity.getPayDetailNo());
        accctransPayRequest.setPayOrderNo(payDetailEntity.getPayTradeItemsNo());
        accctransPayRequest.setBizOrderNo(payDetailEntity.getBizTradeNo());
        accctransPayRequest.setAmount(payDetailEntity.getPayAmount());
        accctransPayRequest.setCurrencyCode(payDetailEntity.getCurrency());
        accctransPayRequest.setTransCode(TransCodeEnum.PAY.getCode());
        accctransPayRequest.setSubTransCode(TransSubCodeEnum.PAY_INNERPAYTOOL_PAY.getCode());
        accctransPayRequest.setRemark("营销活动支付");
        accctransPayRequest.setRequestTime(DateUtils.formatDate(new Date(), "yyyyMMddHHmmss"));

        accctransPayRequest.setMerchantNo(String.valueOf(payDetailEntity.getMerchantNo()));
        return accctransPayRequest;
    }

    /**
     * 封装账务支付失败返回结果
     *
     * @return AccountWithdrawOperationResult
     */
    private PayOperationResult buildActivePayFailResult() {
        PayOperationResult activeOperationResult = new PayOperationResult();
        activeOperationResult.setSuccess(false);
        activeOperationResult.setOperateResultCode(OperationStatusEnum.UNKNOW);
        activeOperationResult.setErrorCode(ErrorEnum.UN_KNOW_EXCEPTION.getCode());
        activeOperationResult.setErrorMessage(ErrorEnum.UN_KNOW_EXCEPTION.getDescription());
        return activeOperationResult;
    }


}
