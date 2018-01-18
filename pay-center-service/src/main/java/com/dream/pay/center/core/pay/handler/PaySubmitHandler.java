package com.dream.pay.center.core.pay.handler;

import com.dream.pay.center.api.request.PaySubmitRequest;
import com.dream.pay.center.api.response.PayDetailResult;
import com.dream.pay.center.api.response.PaySubmitResult;
import com.dream.pay.center.common.enums.ErrorEnum;
import com.dream.pay.center.common.enums.OperatorEnum;
import com.dream.pay.center.common.enums.PayMode;
import com.dream.pay.center.common.exception.BusinessException;
import com.dream.pay.center.core.pay.service.PayCoreService;
import com.dream.pay.center.model.FundsTradeInfoEntity;
import com.dream.pay.center.model.FundsTradeItemsEntity;
import com.dream.pay.center.service.context.FundsBaseContextHolder;
import com.dream.pay.center.service.context.FundsPayContext;
import com.dream.pay.center.service.handler.TransactionAbstractHandler;
import com.dream.pay.enums.PayToolType;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;

/**
 * 提交支付处理器.
 * <p>
 *
 * @author mengzhenbin
 * @version PaySubmitHandler.java
 *          2017-01-11 13:21
 */
@Component
@Data
@Slf4j
public class PaySubmitHandler extends
        TransactionAbstractHandler<PaySubmitRequest, PaySubmitResult> {

    //支付核心接口
    @Resource
    protected PayCoreService payCoreService;

    /**
     * {@inheritDoc}
     */
    @Override
    protected PaySubmitResult execute(PaySubmitRequest paySubmitRequest) {

        //判断主要支付工具类型
        PayToolType mainType = PayMode.select(paySubmitRequest.getPayMode());
        if (null == mainType || PayToolType.ACTIVE_PAY.equals(mainType)) {
            log.error("支付模式[{}]不支持", paySubmitRequest.getPayMode());
            throw new BusinessException(ErrorEnum.PAY_MODE_NOT_SUPPORT);
        }

        //初始化执行上下文
        initContext(OperatorEnum.UNIFORM_PAYMENT);
        FundsPayContext fundsPayContext = (FundsPayContext) FundsBaseContextHolder.get();
        fundsPayContext.setPayMode(PayMode.getByCode(paySubmitRequest.getPayMode()));

        wrapWithTransaction(status -> {
            try {
                payCoreService.createPayInfo(paySubmitRequest);
                return Boolean.TRUE;
            } catch (BusinessException e) {
                status.setRollbackOnly();
                throw e;
            } catch (Exception e) {
                status.setRollbackOnly();
                log.error("支付提交-支付单存储-发生异常-{}", paySubmitRequest, e);
                return Boolean.FALSE;
            }
        });

        PaySubmitResult paySubmitResult = this.buildBasePaySubmitResult();

        wrapWithTransaction(status -> {
            try {
                payCoreService.submitPay(fundsPayContext, paySubmitResult);
                return Boolean.TRUE;
            } catch (BusinessException e) {
                status.setRollbackOnly();
                throw e;
            } catch (Exception e) {
                status.setRollbackOnly();
                log.error("支付提交-支付指令处理-发生异常-{}", paySubmitRequest, e);
                return Boolean.FALSE;
            }
        });
        //返回结果
        return paySubmitResult;
    }

    /**
     * 初始化上下文
     *
     * @param operatorEnum 操作类型
     */
    protected void initContext(OperatorEnum operatorEnum) {
        FundsPayContext fundsPayContext = new FundsPayContext();
        fundsPayContext.setOperatorEnum(operatorEnum);
        FundsBaseContextHolder.set(fundsPayContext);
    }

    /**
     * 组装成功返回结果
     *
     * @return DataResult<PaySubmitResult>
     */
    private PaySubmitResult buildBasePaySubmitResult() {
        PaySubmitResult paySubmitResult = new PaySubmitResult();
        FundsPayContext fundsPayContext = (FundsPayContext) FundsBaseContextHolder.get();
        FundsTradeInfoEntity fundsTradeInfoEntity = fundsPayContext.getFundsTradeInfoEntity();
        FundsTradeItemsEntity fundsTradeItemsEntity = fundsPayContext.getFundsTradeItemsEntity();
        paySubmitResult.setBizTradeNo(fundsTradeInfoEntity.getBizTradeNo());
        paySubmitResult.setPayTradeNo(fundsTradeInfoEntity.getPayTradeNo());
        paySubmitResult.setPayItemNo(fundsTradeItemsEntity.getPayTradeItemsNo());
        paySubmitResult.setPayAmount(fundsTradeInfoEntity.getTradeAmount());
        paySubmitResult.setPayDetailResultList(new ArrayList<PayDetailResult>());
        return paySubmitResult;
    }
}
