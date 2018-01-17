package com.dream.pay.center.pay.service;


import com.dream.center.out.mock.dto.PayOperationResult;
import com.dream.pay.center.api.request.PaySubmitRequest;
import com.dream.pay.center.api.response.PaySubmitResult;
import com.dream.pay.center.common.exception.BusinessException;
import com.dream.pay.center.service.context.FundsPayContext;

/**
 * @author mengzhenbin
 * @Date 2015年4月20日
 * @Company MLS
 * @Description
 */
public interface PayCoreService {

    /**
     * @param paySubmitRequest
     * @return Boolean
     * @throws BusinessException
     * @Date 2015年4月20日
     * @author
     * @Description 创建支付订单及子单信息
     */
    Boolean createPayInfo(PaySubmitRequest paySubmitRequest) throws BusinessException;

    /**
     * @param payContext
     * @param paySubmitResult
     * @return PayOperationResult
     * @throws BusinessException
     * @Date 2015年4月20日
     * @author
     * @Description 提交支付信息，[平台／网银支付]
     */
    void submitPay(FundsPayContext payContext, PaySubmitResult paySubmitResult) throws BusinessException;

    /**
     * @param fundsPayContext
     * @param payOperationResult
     * @return
     * @throws BusinessException
     * @Date 2015年4月20日
     * @author
     * @Description 支付信息提交后处理，[余额支付]
     */
    void confirmPay(FundsPayContext fundsPayContext, PayOperationResult payOperationResult) throws BusinessException;

    /**
     * @param fundsPayContext
     * @return
     * @throws BusinessException
     * @Date 2015年4月20日
     * @author
     * @Description 业务单状态流转
     */
    void updateTradeStatus(FundsPayContext fundsPayContext) throws BusinessException;
}
