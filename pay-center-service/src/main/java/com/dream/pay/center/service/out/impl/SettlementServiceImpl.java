package com.dream.pay.center.service.out.impl;

import com.dream.center.out.mock.dto.*;
import com.dream.center.out.mock.facade.SettlementRefundService;
import com.dream.pay.center.common.enums.ErrorEnum;
import com.dream.pay.center.model.FundsRefundDetailEntity;
import com.dream.pay.center.model.FundsTradeInfoEntity;
import com.dream.pay.center.model.FundsTradeItemsEntity;
import com.dream.pay.center.service.out.SettlementService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 调用【清结算】接口
 *
 * @author mengzhenbin
 * @since 2017-11-17
 */
@Slf4j
@Service
public class SettlementServiceImpl implements SettlementService {

    @Override
    public AccountRefundOperationResult refundFreeze(FundsTradeItemsEntity fundsTradeItemsEntity, List<FundsRefundDetailEntity> fundsRefundDetailEntityList) {
        PretreatmentRequest pretreatmentRequest = null;
        try {
            pretreatmentRequest = buildPretreatmentRequest(fundsTradeItemsEntity, fundsRefundDetailEntityList);
            log.info("调用清结算-退款预处理-请求参数-[{}]", pretreatmentRequest);
            AccountRefundOperationResult result = SettlementRefundService.refundFreeze(pretreatmentRequest);
            log.info("调用清结算-退款预处理-返回结果-[{}]", result);
            return result;
        } catch (Exception e) {
            log.error("调用清结算-退款预处理-发生异常-[{}]", pretreatmentRequest, e);
            return buildAccountOperationFailResult();
        }
    }

    @Override
    public AccountRefundOperationResult refundUnFreeze(FundsRefundDetailEntity fundsRefundDetailEntity) {
        ProcessedRequest processedRequest = null;
        try {
            processedRequest = buildProcessedRequest(fundsRefundDetailEntity);
            log.info("调用清结算-退款成功处理-请求参数-[{}]", processedRequest);
            AccountRefundOperationResult result = SettlementRefundService.refundUnFreeze(processedRequest);
            log.info("调用清结算-退款成功处理-返回结果-[{}]", result);
            return result;
        } catch (Exception e) {
            log.error("调用清结算-退款预处理-发生异常-[{}]", processedRequest, e);
            return buildAccountOperationFailResult();
        }
    }

    private ProcessedRequest buildProcessedRequest(FundsRefundDetailEntity fundsRefundDetailEntity) {
        ProcessedRequest processedRequest = new ProcessedRequest();
        processedRequest.setOutBizNo(fundsRefundDetailEntity.getBizTradeNo());
        processedRequest.setAcquireNo(fundsRefundDetailEntity.getPayTradeNo());
        processedRequest.setPayDetailNo(fundsRefundDetailEntity.getPayDetailNo());
        processedRequest.setRefundDetailNo(fundsRefundDetailEntity.getRefundDetailNo());
        return processedRequest;
    }

    /**
     * 封装账务操作接口参数
     *
     * @param fundsTradeItemsEntity
     * @param fundsRefundDetailEntityList
     * @return PretreatmentRequest
     */
    private PretreatmentRequest buildPretreatmentRequest(FundsTradeItemsEntity fundsTradeItemsEntity, List<FundsRefundDetailEntity> fundsRefundDetailEntityList) {
        PretreatmentRequest pretreatmentRequest = new PretreatmentRequest();
        pretreatmentRequest.setMerchantNo(String.valueOf(fundsTradeItemsEntity.getMerchantNo()));

        List<PrestreatmentInfo> detailList = new ArrayList<PrestreatmentInfo>();

        for (FundsRefundDetailEntity fundsRefundDetailEntity : fundsRefundDetailEntityList) {
            PrestreatmentInfo prestreatmentInfo = new PrestreatmentInfo();
            prestreatmentInfo.setOutBizNo(fundsRefundDetailEntity.getBizTradeNo());
            prestreatmentInfo.setPayTradeNo(fundsRefundDetailEntity.getPayTradeNo());
            prestreatmentInfo.setPayDetailNo(fundsRefundDetailEntity.getPayDetailNo());
            prestreatmentInfo.setRefundDetailNo(fundsRefundDetailEntity.getRefundDetailNo());
            prestreatmentInfo.setRefundAmount(fundsTradeItemsEntity.getTradeAmount());
            detailList.add(prestreatmentInfo);
        }

        pretreatmentRequest.setPrestreatmentInfoList(detailList);
        pretreatmentRequest.setRemark("退款预处理");
        return pretreatmentRequest;
    }

    /**
     * 封装账务操作失败返回结果
     *
     * @return AccountWithdrawOperationResult
     */
    private AccountRefundOperationResult buildAccountOperationFailResult() {
        AccountRefundOperationResult accountOperationResult = new AccountRefundOperationResult();
        accountOperationResult.setSuccess(false);
        accountOperationResult.setOperateResultCode(OperationStatusEnum.UNKNOW);
        accountOperationResult.setErrorCode(ErrorEnum.UN_KNOW_EXCEPTION.getCode());
        accountOperationResult.setErrorMessage(ErrorEnum.UN_KNOW_EXCEPTION.getDescription());
        return accountOperationResult;
    }
}
