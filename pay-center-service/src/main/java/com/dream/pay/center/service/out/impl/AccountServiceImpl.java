package com.dream.pay.center.service.out.impl;

import com.dream.center.out.mock.dto.*;
import com.dream.center.out.mock.enums.TransCodeEnum;
import com.dream.center.out.mock.enums.TransSubCodeEnum;
import com.dream.center.out.mock.facade.AcctransPayService;
import com.dream.center.out.mock.facade.AcctransWithdrawService;
import com.dream.pay.center.common.enums.ErrorEnum;
import com.dream.pay.center.model.FundsPayDetailEntity;
import com.dream.pay.center.model.FundsWithdrawDetailEntity;
import com.dream.pay.center.service.out.AccountService;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.utils.DateUtils;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * 调用【账务】接口
 *
 * @author mengzhenbin
 * @since 2017-11-17
 */
@Slf4j
@Service
public class AccountServiceImpl implements AccountService {

    @Override
    public AccountWithdrawOperationResult withdrawTransOut(FundsWithdrawDetailEntity fundsWithdrawDetailEntity) {
        AcctransWithdrawRequest accctransWithdrawRequest = null;
        try {
            accctransWithdrawRequest = buildAcctransWithdrawRequest(fundsWithdrawDetailEntity);
            accctransWithdrawRequest.setSubTransCode(TransSubCodeEnum.WITHDRAW_PRE_WITHDRAW.getCode());
            accctransWithdrawRequest.setRemark("提现预处理-出账");
            log.info("调用账务-提现预处理出账-请求参数-[{}]", accctransWithdrawRequest);
            AccountWithdrawOperationResult result = AcctransWithdrawService.withdraw(accctransWithdrawRequest);
            log.info("调用账务-提现预处理出账-返回结果-[{}]", result);
            return result;
        } catch (Exception e) {
            log.error("调用账务-提现预处理出账-发生异常-[{}]", accctransWithdrawRequest, e);
            return buildAccountWithdrawFailResult();
        }
    }

    @Override
    public AccountWithdrawOperationResult withdrawTransIn(FundsWithdrawDetailEntity fundsWithdrawDetailEntity) {
        try {
            AcctransWithdrawRequest accctransWithdrawRequest = buildAcctransWithdrawRequest(fundsWithdrawDetailEntity);
            accctransWithdrawRequest.setSubTransCode(TransSubCodeEnum.WITHDRAW_FAIL.getCode());
            accctransWithdrawRequest.setRemark("提现失败-入账");
            log.info("调用账务-提现成功入账-请求参数-[{}]", accctransWithdrawRequest);
            AccountWithdrawOperationResult result = AcctransWithdrawService.withdraw(accctransWithdrawRequest);
            log.info("调用账务-提现成功入账-返回结果-[{}]", result);
            return result;
        } catch (Exception e) {
            log.error("调用账务-提现成功入账-发生异常-[{}]", e);
            return buildAccountWithdrawFailResult();
        }
    }

    @Override
    public PayOperationResult payTransOut(FundsPayDetailEntity payDetailEntity) {
        try {
            AcctransPayRequest acctransPayRequest = buildAcctransPayRequest(payDetailEntity);
            log.info("调用账务-余额支付-请求参数-[{}]", acctransPayRequest);
            PayOperationResult result = AcctransPayService.balancePay(acctransPayRequest);
            log.info("调用账务-余额支付-返回结果-[{}]", result);
            return result;
        } catch (Exception e) {
            log.error("调用账务-余额支付-发生异常-[{}]", e);
            return buildAccountPayFailResult();
        }
    }

    /**
     * 封装账务操作接口参数
     *
     * @param payDetailEntity
     * @return AcctransPayRequest
     */
    private AcctransPayRequest buildAcctransPayRequest(FundsPayDetailEntity payDetailEntity) {
        AcctransPayRequest accctransPayRequest = new AcctransPayRequest();
        accctransPayRequest.setAppName("收单-提现");
        accctransPayRequest.setClientId(payDetailEntity.getPayDetailNo());
        accctransPayRequest.setPayOrderNo(payDetailEntity.getPayTradeNo());
        accctransPayRequest.setBizOrderNo(payDetailEntity.getBizTradeNo());
        accctransPayRequest.setAmount(payDetailEntity.getPayAmount());
        accctransPayRequest.setCurrencyCode(payDetailEntity.getCurrency());
        accctransPayRequest.setTransCode(TransCodeEnum.PAY.getCode());
        accctransPayRequest.setSubTransCode(TransSubCodeEnum.PAY_INNERPAYTOOL_PAY.getCode());
        accctransPayRequest.setRemark("余额支付");
        accctransPayRequest.setRequestTime(DateUtils.formatDate(new Date(), "yyyyMMddHHmmss"));

        accctransPayRequest.setMerchantNo(String.valueOf(payDetailEntity.getMerchantNo()));
        return accctransPayRequest;
    }


    /**
     * 封装账务操作接口参数
     *
     * @param fundsWithdrawDetailEntity
     * @return AcctransWithdrawRequest
     */
    private AcctransWithdrawRequest buildAcctransWithdrawRequest(FundsWithdrawDetailEntity fundsWithdrawDetailEntity) {
        AcctransWithdrawRequest accctransWithdrawRequest = new AcctransWithdrawRequest();
        accctransWithdrawRequest.setAppName("收单-提现");
        accctransWithdrawRequest.setClientId(fundsWithdrawDetailEntity.getWithdrawDetailNo());
        accctransWithdrawRequest.setPayOrderNo(fundsWithdrawDetailEntity.getPayTradeNo());
        accctransWithdrawRequest.setBizOrderNo(fundsWithdrawDetailEntity.getBizTradeNo());
        accctransWithdrawRequest.setAmount(fundsWithdrawDetailEntity.getWithdrawAmount());
        accctransWithdrawRequest.setCurrencyCode(fundsWithdrawDetailEntity.getCurrency());
        accctransWithdrawRequest.setTransCode(TransCodeEnum.WITHDRAW.getCode());
        accctransWithdrawRequest.setRequestTime(DateUtils.formatDate(new Date(), "yyyyMMddHHmmss"));

        accctransWithdrawRequest.setMerchantNo(String.valueOf(fundsWithdrawDetailEntity.getMerchantNo()));
        accctransWithdrawRequest.setAccountType(fundsWithdrawDetailEntity.getCustAccountType());
        return accctransWithdrawRequest;
    }


    /**
     * 封装账务操作失败返回结果
     *
     * @return AccountWithdrawOperationResult
     */
    private AccountWithdrawOperationResult buildAccountWithdrawFailResult() {
        AccountWithdrawOperationResult accountWithdrawOperationResult = new AccountWithdrawOperationResult();
        accountWithdrawOperationResult.setSuccess(false);
        accountWithdrawOperationResult.setOperateResultCode(OperationStatusEnum.UNKNOW);
        accountWithdrawOperationResult.setErrorCode(ErrorEnum.UN_KNOW_EXCEPTION.getCode());
        accountWithdrawOperationResult.setErrorMessage(ErrorEnum.UN_KNOW_EXCEPTION.getDescription());
        return accountWithdrawOperationResult;
    }

    /**
     * 封装账务支付失败返回结果
     *
     * @return AccountWithdrawOperationResult
     */
    private PayOperationResult buildAccountPayFailResult() {
        PayOperationResult accountOperationResult = new PayOperationResult();
        accountOperationResult.setSuccess(false);
        accountOperationResult.setOperateResultCode(OperationStatusEnum.UNKNOW);
        accountOperationResult.setErrorCode(ErrorEnum.UN_KNOW_EXCEPTION.getCode());
        accountOperationResult.setErrorMessage(ErrorEnum.UN_KNOW_EXCEPTION.getDescription());
        return accountOperationResult;
    }

}
