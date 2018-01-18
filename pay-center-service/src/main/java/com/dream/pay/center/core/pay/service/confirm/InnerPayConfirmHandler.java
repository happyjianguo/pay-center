package com.dream.pay.center.core.pay.service.confirm;

import com.dream.center.out.mock.dto.OperationStatusEnum;
import com.dream.center.out.mock.dto.PayOperationResult;
import com.dream.pay.bean.Money;
import com.dream.pay.center.common.exception.BusinessException;
import com.dream.pay.center.dao.FundsPayDetailDao;
import com.dream.pay.center.model.FundsPayDetailEntity;
import com.dream.pay.center.model.FundsTradeInfoEntity;
import com.dream.pay.center.service.context.FundsPayContext;
import com.dream.pay.center.service.out.AccountService;
import com.dream.pay.center.status.FundsPayStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component("innerPayConfirmHandler")
@Slf4j
public class InnerPayConfirmHandler extends AbstractPayConfirmHandler implements ConfirmPayHandler {

    @Resource
    private AccountService accountService;

    @Resource
    private FundsPayDetailDao fundsPayDetailDao;

    @Override
    public void confirm(FundsPayContext fundsPayContext, PayOperationResult payOperationResult) throws BusinessException {
        OperationStatusEnum preDetailStatus = fundsPayContext.getPreDetailStatus();
        FundsPayDetailEntity currentDetail = fundsPayContext.getCurrentDetail();
        log.info("[虚拟账户支付]-[支付确认]-处理开始|本次处理支付单[{}],前置单处理状态为[{}]", currentDetail, preDetailStatus);

        if (OperationStatusEnum.SUCCESS.equals(preDetailStatus)) {
            PayOperationResult currentPayOperationResult = accountService.payTransOut(currentDetail);

            //设置支付详情单
            setPayInfoDetailResult(fundsPayContext, currentPayOperationResult);

            //更新支付详情单
            fundsPayDetailDao.updateByPrimaryKeySelective(currentDetail);

            //累加实际支付金额
            fundsPayContext.setRealPayAmount(fundsPayContext.getRealPayAmount().addTo(new Money(currentPayOperationResult.getRealAmount())));
            log.info("[虚拟账户支付]-[支付确认]-实际支付金额为[{}]", fundsPayContext.getRealPayAmount());
            //设置前置支付单操作返回码
            fundsPayContext.setPreDetailStatus(currentPayOperationResult.getOperateResultCode());
        } else {
            fundsPayContext.setPreDetailStatus(OperationStatusEnum.PROCESSING);
            log.info("前置支付单操作状态为{}，不满足当前支付单执行条件", preDetailStatus);
        }

        log.info("[虚拟账户支付]-[支付确认]-处理结束|本次处理结果-{}", currentDetail);

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

        FundsPayStatus fundsPayStatus = FundsPayStatus.convertByOperateCode(payOperationResult.getOperateResultCode());
        currentDetail.setPayStatus(fundsPayStatus.getCode());

        currentDetail.setRealAmount(payOperationResult.getRealAmount());
        currentDetail.setBizChannel(payOperationResult.getBizChannel().getCode());
        currentDetail.setChannelReturnNo(payOperationResult.getPayVoucherNo());
        currentDetail.setOutReturnNo(payOperationResult.getPayVoucherNo());
        currentDetail.setOutFinishTime(payOperationResult.getAccountingTime());
        currentDetail.setPayNote(super.buildPayNote(fundsTradeInfoEntity, currentDetail));

        currentDetail.setOutErrorCode(payOperationResult.getErrorCode());
        currentDetail.setOutErrorMsg(payOperationResult.getErrorMessage());
    }
}
