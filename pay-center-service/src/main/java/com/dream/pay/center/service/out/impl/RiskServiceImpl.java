package com.dream.pay.center.service.out.impl;

import com.dream.center.out.mock.facade.RiskCheckWithdrawService;
import com.dream.pay.bean.DataResult;
import com.dream.pay.center.common.enums.ErrorEnum;
import com.dream.pay.center.common.exception.BusinessException;
import com.dream.pay.center.service.out.RiskService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 调用【风控】接口
 *
 * @author mengzhenbin
 * @since 2017-11-17
 */
@Slf4j
@Service
public class RiskServiceImpl implements RiskService {

    @Override
    public void riskCheck(String merchantNo) {
        DataResult<Integer> riskResult = null;
        log.info("调用风控-黑名单-请求参数-[{}]", merchantNo);
        try {
            riskResult = RiskCheckWithdrawService.withdrawBlackCheck(merchantNo);
        } catch (Exception e) {
            log.error("调用风控-黑名单-发生异常-[{}]", merchantNo, e);
        }
        log.info("调用风控-黑名单-返回结果-[{}-{}],[{}]", riskResult.getCode(), riskResult.getMessage(), riskResult.getData());
        if (null == riskResult || !riskResult.isSuccess() || null == riskResult.getData() || riskResult.getData() != 0) {
            throw new BusinessException(ErrorEnum.RISK_WITHDRAW_APPLY);
        }
    }
}