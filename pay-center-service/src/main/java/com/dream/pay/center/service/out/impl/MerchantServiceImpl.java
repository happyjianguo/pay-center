package com.dream.pay.center.service.out.impl;

import com.dream.center.out.mock.dto.BankCardInfoResult;
import com.dream.center.out.mock.dto.GetBankCardInfoRequest;
import com.dream.center.out.mock.facade.BankCardInfoService;
import com.dream.pay.bean.DataResult;
import com.dream.pay.center.common.enums.ErrorEnum;
import com.dream.pay.center.common.exception.BusinessException;
import com.dream.pay.center.service.out.MerchantService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 调用【商户平台】接口
 *
 * @author mengzhenbin
 * @since 2017-11-17
 */
@Slf4j
@Service
public class MerchantServiceImpl implements MerchantService {

    @Override
    public BankCardInfoResult queryCardInfo(String merchantNo, String bindCardId, int actionTypeCode) {

        log.info("调用商户平台-查询卡信息-请求参数-[绑卡ID={},商户号={}]", bindCardId, merchantNo);
        GetBankCardInfoRequest getBankCardInfoRequest = new GetBankCardInfoRequest();
        getBankCardInfoRequest.setActionTypeCode(actionTypeCode);
        getBankCardInfoRequest.setCardBindId(Long.valueOf(bindCardId));
        getBankCardInfoRequest.setUserNo(merchantNo);
        DataResult<BankCardInfoResult> bankCardInfoDataResult = null;
        try {
            bankCardInfoDataResult = BankCardInfoService.queryCardInfo(getBankCardInfoRequest);
        } catch (Exception e) {
            log.error("调用商户平台-查询卡信息-发生异常-[绑卡ID={},商户号={}]", bindCardId, merchantNo, e);
        }
        log.info("调用商户平台-查询卡信息-返回结果-[{}-{}],[{}]", bankCardInfoDataResult.getCode(), bankCardInfoDataResult.getMessage(), bankCardInfoDataResult.getData());
        if (!bankCardInfoDataResult.isSuccess() || bankCardInfoDataResult == null) {
            throw new BusinessException(ErrorEnum.QUERY_CARD_INFO_ERROR);
        }
        return bankCardInfoDataResult.getData();
    }
}
