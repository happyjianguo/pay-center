package com.dream.pay.center.pay.handler.confirm;

import com.dream.center.out.mock.dto.OperationStatusEnum;
import com.dream.center.out.mock.dto.PayOperationResult;
import com.dream.pay.center.api.request.WithdrawApplyRequest;
import com.dream.pay.center.common.constants.UnifiedBizCode;
import com.dream.pay.center.common.core.Money;
import com.dream.pay.center.common.enums.BizActionEnum;
import com.dream.pay.center.common.enums.BizModeEnum;
import com.dream.pay.center.common.enums.PayTool;
import com.dream.pay.center.common.exception.BusinessException;
import com.dream.pay.center.dao.FundsPayDetailDao;
import com.dream.pay.center.dao.FundsTradeInfoDao;
import com.dream.pay.center.model.FundsPayDetailEntity;
import com.dream.pay.center.model.FundsTradeInfoEntity;
import com.dream.pay.center.service.context.FundsPayContext;
import com.dream.pay.center.status.FundsPayStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;

@Component("bankPayConfirmHandler")
@Slf4j
public class BankPayConfirmHandler extends AbstractPayConfirmHandler implements ConfirmPayHandler {

    @Resource
    private FundsPayDetailDao fundsPayDetailDao;


    @Override
    public void confirm(FundsPayContext fundsPayContext, PayOperationResult payOperationResult) throws BusinessException {
        OperationStatusEnum preDetailStatus = fundsPayContext.getPreDetailStatus();
        FundsPayDetailEntity currentDetail = fundsPayContext.getCurrentDetail();
        log.info("[网关支付]-[支付确认]-处理开始|本次处理支付单[{}],前置单处理状态为[{}]", currentDetail, preDetailStatus);

        //设置支付详情单
        setPayInfoDetailResult(fundsPayContext, payOperationResult);

        //更新支付详情单
        fundsPayDetailDao.updateByPrimaryKeySelective(currentDetail);

        //累加实际支付金额
        fundsPayContext.setRealPayAmount(fundsPayContext.getRealPayAmount().addTo(new Money(payOperationResult.getRealAmount())));
        log.info("[网关支付]-[支付确认]-实际支付金额为[{}]", fundsPayContext.getRealPayAmount());
        //设置前置支付单操作状态返回码
        fundsPayContext.setPreDetailStatus(payOperationResult.getOperateResultCode());

        log.info("[网关支付]-[支付确认]-处理结束|本次处理结果-{}", currentDetail);

    }

    /**
     * 设置detail状态
     *
     * @param fundsPayContext
     * @param payOperationResult
     */

    private void setPayInfoDetailResult(FundsPayContext fundsPayContext, PayOperationResult payOperationResult) {
        FundsPayDetailEntity currentDetail = fundsPayContext.getCurrentDetail();
        FundsTradeInfoEntity fundsTradeInfoEntity = fundsPayContext.getFundsTradeInfoEntity();

        //设置当前子单处理信息
        FundsPayStatus fundsPayStatus = FundsPayStatus.convertByOperateCode(payOperationResult.getOperateResultCode());
        currentDetail.setPayStatus(fundsPayStatus.getCode());

        currentDetail.setRealAmount(payOperationResult.getRealAmount());
        currentDetail.setBizChannel(payOperationResult.getBizChannel().getCode());
        currentDetail.setChannelReturnNo(payOperationResult.getPayChannelNo());
        currentDetail.setOutReturnNo(payOperationResult.getPayBankNo());
        currentDetail.setOutFinishTime(payOperationResult.getPaymentTime());
        currentDetail.setPayNote(super.buildPayNote(fundsTradeInfoEntity, currentDetail));

        currentDetail.setOutErrorCode(payOperationResult.getErrorCode());
        currentDetail.setOutErrorMsg(payOperationResult.getErrorMessage());
    }
}
