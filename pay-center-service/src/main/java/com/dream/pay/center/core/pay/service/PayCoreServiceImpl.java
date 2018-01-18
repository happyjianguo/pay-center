package com.dream.pay.center.core.pay.service;


import com.dream.center.out.mock.dto.OperationStatusEnum;
import com.dream.center.out.mock.dto.PayOperationResult;
import com.dream.pay.bean.Money;
import com.dream.pay.center.api.request.PayDetailRequest;
import com.dream.pay.center.api.request.PaySubmitRequest;
import com.dream.pay.center.api.response.PaySubmitResult;
import com.dream.pay.center.common.enums.ErrorEnum;
import com.dream.pay.center.common.enums.PayMode;
import com.dream.pay.center.common.enums.PayTagEnum;
import com.dream.pay.center.common.exception.BaseException;
import com.dream.pay.center.common.exception.BusinessException;
import com.dream.pay.center.core.pay.service.confirm.ConfirmPayHandler;
import com.dream.pay.center.core.pay.service.submit.SubmitPayHandler;
import com.dream.pay.center.dao.FundsPayDetailDao;
import com.dream.pay.center.dao.FundsTradeInfoDao;
import com.dream.pay.center.dao.FundsTradeItemsDao;
import com.dream.pay.center.model.FundsPayDetailEntity;
import com.dream.pay.center.model.FundsTradeInfoEntity;
import com.dream.pay.center.model.FundsTradeItemsEntity;
import com.dream.pay.center.service.context.FundsBaseContextHolder;
import com.dream.pay.center.service.context.FundsPayContext;
import com.dream.pay.center.service.sequence.SerialidGenerator;
import com.dream.pay.center.status.FundsPayStatus;
import com.dream.pay.center.status.FundsTradeItemStatus;
import com.dream.pay.center.status.FundsTradeStatus;
import com.dream.pay.enums.BizActionEnum;
import com.dream.pay.enums.InOutEnum;
import com.dream.pay.enums.PayToolType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * @author mengzhenbin
 * @Date 2015年4月20日
 * @Company MLS
 * @Description
 */
@Component
@Slf4j
public class PayCoreServiceImpl implements PayCoreService {

    //收单－存储服务
    @Resource
    private FundsTradeInfoDao fundsTradeInfoDao;
    @Resource
    private FundsTradeItemsDao fundsTradeItemsDao;
    @Resource
    private FundsPayDetailDao fundsPayDetailDao;
    @Resource
    protected SerialidGenerator serialidGenerator;
    @Resource
    protected ServiceLocator serviceLocator;


    /**
     * @param paySubmitRequest
     * @return
     * @throws BusinessException
     * @Date 2015年4月20日
     * @author
     * @Description 创建支付订单及子单信息
     */
    public Boolean createPayInfo(PaySubmitRequest paySubmitRequest) throws BusinessException {

        //判断业务单是否存在
        FundsTradeInfoEntity fundsTradeInfoEntity = fundsTradeInfoDao.selectByBizNoAndMerchantNo(paySubmitRequest.getBizTradeNo(), paySubmitRequest.getMerchantNo());
        if (null == fundsTradeInfoEntity) {
            log.error("[{}]-[{}]收单不存在", paySubmitRequest.getMerchantNo(), paySubmitRequest.getBizTradeNo());
            throw new BusinessException(ErrorEnum.BIZ_ORDER_NOT_EXIST);
        }

        if (!fundsTradeInfoEntity.getPayTradeNo().equals(paySubmitRequest.getPayTradeNo())) {
            log.error("[{}]-[{}]支付收单号不合法", fundsTradeInfoEntity.getPayTradeNo(), paySubmitRequest.getPayTradeNo());
            throw new BusinessException(ErrorEnum.PAY_ORDER_NO_ERROR);
        }

        if (FundsTradeStatus.SUCCEED.getCode().equals(fundsTradeInfoEntity.getTradeState())) {
            log.error("[{}]-[{}]支付收单状态不合法，请勿重复提交支付", fundsTradeInfoEntity.getPayTradeNo(), paySubmitRequest.getPayTradeNo());
            throw new BusinessException(ErrorEnum.PAY_STATUS_ERROR);
        }

        //金额校验。包括业务单与支付单，支付单与详情单
        amountCheck(paySubmitRequest, fundsTradeInfoEntity);

        //生成并且存储支付单
        storePayInfo(paySubmitRequest, fundsTradeInfoEntity);

        return Boolean.TRUE;
    }

    /**
     * @param
     * @param payContext
     * @param paySubmitResult
     * @return PayOperationResult
     * @throws BusinessException
     * @Date 2015年4月20日
     * @author
     * @Description 提交支付信息，[平台／网银支付]
     */
    public void submitPay(FundsPayContext payContext, PaySubmitResult paySubmitResult) throws BusinessException {
        List<FundsPayDetailEntity> payDetails = payContext.getFundsPayDetailEntitys();
        log.info("[支付核心处理]-[支付提交]|处理开始");
        for (FundsPayDetailEntity detail : payDetails) {
            PayToolType payToolType = PayToolType.toPayToolType(detail.getPayToolType());
            log.info("当前待处理单支付工具类型为[{}]", payToolType);
            String serviceName = "";
            switch (payToolType) {
                case QUICK_PAY:
                    serviceName = "quickPaySubmitPayHandler";
                    break;
                case PLAT_PAY:
                case BANK_PAY:
                    serviceName = "gatePaySubmitPayHandler";
                    break;
                case INNER_PAY:
                    serviceName = "innerPaySubmitPayHandler";
                case ACTIVE_PAY:
                    break;
            }
            log.debug("[支付核心处理]-[支付提交]|支付工具处理类[{}]", serviceName);
            if (!"".equals(serviceName)) {
                payContext.setCurrentDetail(detail);
                SubmitPayHandler handler = (SubmitPayHandler) serviceLocator.getObject(serviceName);
                handler.submitPay(payContext, paySubmitResult);
            }
        }

        log.info("[支付核心处理]-[支付提交]|处理结束");
    }


    /**
     * @param fundsPayContext
     * @param payOperationResult
     * @return PayOperationResult
     * @throws BusinessException
     * @Date 2015年4月20日
     * @author
     * @Description 支付信息提交后处理，［外部/内部／活动工具支付]
     */
    public void confirmPay(FundsPayContext fundsPayContext, PayOperationResult payOperationResult) throws BusinessException {
        OperationStatusEnum preDetailStatus = fundsPayContext.getPreDetailStatus();
        Collections.sort(fundsPayContext.getFundsPayDetailEntitys());// 对支付子单进行排序，优先处理银行支付单
        log.info("[支付核心处理]-[支付确认]|处理开始|前置支付单处理结果为[{}]", preDetailStatus);
        if (OperationStatusEnum.SUCCESS.equals(preDetailStatus) || OperationStatusEnum.FAIL.equals(preDetailStatus)) {// 成功或者失败状态需要更新所又子单状态到成功或者失败
            for (FundsPayDetailEntity detail : fundsPayContext.getFundsPayDetailEntitys()) {
                PayToolType payToolType = PayToolType.toPayToolType(detail.getPayToolType());
                log.info("当前待处理支付单支付工具类型为[{}]", payToolType);
                String serviceName = "";
                switch (payToolType) {
                    case QUICK_PAY:
                    case PLAT_PAY:
                    case BANK_PAY:
                        serviceName = "bankPayConfirmHandler";// 快捷，平台，网银支付子单处理类
                        break;
                    case INNER_PAY:
                        serviceName = "innerPayConfirmHandler";// 余额支付子单处理类
                        break;
                    case ACTIVE_PAY:
                        serviceName = "activePayConfirmHandler";// 活动支付子单处理类
                    default:
                        break;
                }
                log.debug("[支付核心处理]-[支付确认]|支付工具处理类[{}]", serviceName);
                if (!"".equals(serviceName)) {
                    fundsPayContext.setCurrentDetail(detail);
                    ConfirmPayHandler confirmPayHandler = (ConfirmPayHandler) serviceLocator.getObject(serviceName);
                    confirmPayHandler.confirm(fundsPayContext, payOperationResult);
                }
            }
        }

        FundsTradeItemsEntity fundsTradeItemsEntity = fundsPayContext.getFundsTradeItemsEntity();
        FundsTradeInfoEntity fundsTradeInfoEntity = fundsPayContext.getFundsTradeInfoEntity();

        PayTagEnum payTag = fundsPayContext.getPayTag();
        fundsTradeItemsEntity.setTradeTag(null != payTag ? payTag.name() : "");

        OperationStatusEnum lastOperationStatusEnum = fundsPayContext.getPreDetailStatus();

        log.info("[支付核心处理]-[支付确认]|处理开始|后置支付单处理结果为[{}],支付标签为[{}]", lastOperationStatusEnum, payTag);

        switch (lastOperationStatusEnum) {
            case FAIL://失败
                fundsTradeItemsEntity.setTradeState(FundsTradeItemStatus.FAILED.getCode());
                fundsTradeItemsEntity.setUpdateTime(new Date());

                fundsTradeInfoEntity.setRealAmount(fundsPayContext.getRealPayAmount().getCent());
                fundsTradeInfoEntity.setTradeState(FundsTradeStatus.FAILED.getCode());
                fundsTradeInfoEntity.setPaymentTime(new Date());
                fundsTradeInfoEntity.setUpdateTime(new Date());
                break;
            case SUCCESS://成功
                fundsTradeItemsEntity.setTradeState(FundsTradeItemStatus.SUCCESS.getCode());
                fundsTradeItemsEntity.setUpdateTime(new Date());

                fundsTradeInfoEntity.setRealAmount(fundsPayContext.getRealPayAmount().getCent());
                fundsTradeInfoEntity.setTradeState(FundsTradeStatus.SUCCEED.getCode());
                fundsTradeInfoEntity.setPaymentTime(new Date());
                fundsTradeInfoEntity.setUpdateTime(new Date());
                break;
            case UNKNOW:
            case PROCESSING:
            default:
                break;
        }

        fundsTradeItemsDao.updateByPrimaryKeySelective(fundsTradeItemsEntity);
        fundsTradeInfoDao.updateByPrimaryKeySelective(fundsTradeInfoEntity);
    }

    /**
     * @param fundsPayContext
     * @return
     * @throws BusinessException
     * @Date 2015年4月20日
     * @author
     * @Description 业务单状态流转
     */
    public void updateTradeStatus(FundsPayContext fundsPayContext) {
        FundsTradeItemsEntity fundsTradeItemsEntity = fundsPayContext.getFundsTradeItemsEntity();
        fundsTradeItemsDao.updateStatusByTradeItemNo(FundsTradeItemStatus.DOING.getCode(), fundsTradeItemsEntity.getPayTradeItemsNo());

        FundsTradeInfoEntity fundsTradeInfoEntity = fundsPayContext.getFundsTradeInfoEntity();
        fundsTradeInfoDao.updateStatusByPayTradeNo(FundsTradeStatus.PROCESSING.getCode(), fundsTradeInfoEntity.getPayTradeNo());
    }


    /**
     * 创建支付子单和支付详情单
     *
     * @param paySubmitRequest
     * @param fundsTradeInfoEntity
     * @throws BaseException
     */
    protected void storePayInfo(PaySubmitRequest paySubmitRequest, FundsTradeInfoEntity fundsTradeInfoEntity) throws BaseException {
        //生成子收单信息并存储
        FundsTradeItemsEntity fundsTradeItemsEntity = composeTradeItemInfo(fundsTradeInfoEntity, paySubmitRequest);
        fundsTradeItemsDao.insert(fundsTradeItemsEntity);

        //生成支付详情信息并存储
        List<FundsPayDetailEntity> payDetailEntitys = composePayDetailInfo(fundsTradeInfoEntity, fundsTradeItemsEntity, paySubmitRequest);
        for (FundsPayDetailEntity payDetailEntity : payDetailEntitys) {
            fundsPayDetailDao.insert(payDetailEntity);
        }
        FundsPayContext fundsPayContext = (FundsPayContext) FundsBaseContextHolder.get();
        fundsPayContext.setFundsTradeInfoEntity(fundsTradeInfoEntity);
        fundsPayContext.setFundsTradeItemsEntity(fundsTradeItemsEntity);
        fundsPayContext.setFundsPayDetailEntitys(payDetailEntitys);
    }


    /**
     * 组装支付详情单数据
     *
     * @param fundsTradeInfoEntity
     * @param fundsTradeItemsEntity
     * @param paySubmitRequest
     * @return FundsPayDetailEntity
     */
    private List<FundsPayDetailEntity> composePayDetailInfo(FundsTradeInfoEntity fundsTradeInfoEntity, FundsTradeItemsEntity fundsTradeItemsEntity, PaySubmitRequest paySubmitRequest) {
        List<FundsPayDetailEntity> payDetailEntitys = new ArrayList<FundsPayDetailEntity>();
        for (PayDetailRequest payDetailRequest : paySubmitRequest.getPayDetailInfoList()) {
            FundsPayDetailEntity fundsPayDetailEntity = new FundsPayDetailEntity();
            fundsPayDetailEntity.setPayDetailNo(serialidGenerator.get());
            fundsPayDetailEntity.setPayTradeItemsNo(fundsTradeItemsEntity.getPayTradeItemsNo());
            fundsPayDetailEntity.setPayTradeNo(fundsTradeInfoEntity.getPayTradeNo());
            fundsPayDetailEntity.setBizTradeNo(fundsTradeInfoEntity.getBizTradeNo());
            fundsPayDetailEntity.setPayTool(payDetailRequest.getPayTool().name());
            fundsPayDetailEntity.setPayToolType(payDetailRequest.getPayToolType().key());
            fundsPayDetailEntity.setBizSubAction(payDetailRequest.getPayTool().getBizSubAction());
            fundsPayDetailEntity.setCurrency(Integer.valueOf(paySubmitRequest.getCurrencyCode()));
            fundsPayDetailEntity.setPayAmount(payDetailRequest.getPayAmount());
            fundsPayDetailEntity.setPayStatus(FundsPayStatus.NO_PAY.getCode());
            fundsPayDetailEntity.setUserNo(paySubmitRequest.getUserNo());
            fundsPayDetailEntity.setMerchantNo(paySubmitRequest.getMerchantNo());
            fundsPayDetailEntity.setOutBizContext(paySubmitRequest.getTraceContext().getTraceId());
            fundsPayDetailEntity.setCreateTime(new Date());
            fundsPayDetailEntity.setUpdateTime(new Date());
            payDetailEntitys.add(fundsPayDetailEntity);
        }
        Collections.sort(payDetailEntitys);
        return payDetailEntitys;
    }


    /**
     * 组装业务子单数据
     *
     * @param fundsTradeInfoEntity
     * @param paySubmitRequest
     * @return FundsTradeItemsEntity
     */
    private FundsTradeItemsEntity composeTradeItemInfo(FundsTradeInfoEntity fundsTradeInfoEntity, PaySubmitRequest paySubmitRequest) {
        FundsTradeItemsEntity fundsTradeItemsEntity = new FundsTradeItemsEntity();
        fundsTradeItemsEntity.setPayTradeItemsNo(serialidGenerator.get());
        fundsTradeItemsEntity.setPayTradeNo(fundsTradeInfoEntity.getPayTradeNo());
        fundsTradeItemsEntity.setBizTradeNo(fundsTradeInfoEntity.getBizTradeNo());
        fundsTradeItemsEntity.setBizAction(BizActionEnum.PAY.getCode());
        fundsTradeItemsEntity.setFundsInOut(InOutEnum.IN.getValue());
        fundsTradeItemsEntity.setMode(paySubmitRequest.getPayMode());
        fundsTradeItemsEntity.setTradeAmount(fundsTradeInfoEntity.getTradeAmount());
        fundsTradeItemsEntity.setMerchantNo(fundsTradeInfoEntity.getMerchantNo());
        fundsTradeItemsEntity.setUserNo(fundsTradeInfoEntity.getUserNo());
        fundsTradeItemsEntity.setTradeState(FundsTradeItemStatus.CREATE.getCode());
        fundsTradeItemsEntity.setTradeNote(PayMode.getByCode(paySubmitRequest.getPayMode()).desc());
        fundsTradeItemsEntity.setCreateTime(new Date());
        fundsTradeItemsEntity.setUpdateTime(new Date());
        return fundsTradeItemsEntity;
    }

    /**
     * 金额校验
     *
     * @param paySubmitRequest
     * @param fundsTradeInfoEntity
     * @throws BusinessException
     */
    private void amountCheck(PaySubmitRequest paySubmitRequest, FundsTradeInfoEntity fundsTradeInfoEntity) throws BusinessException {
        if (fundsTradeInfoEntity.getTradeAmount().compareTo(paySubmitRequest.getPayAmount()) != 0) {
            log.error("业务单金额[{}]与支付总金额[{}]不匹配", fundsTradeInfoEntity.getTradeAmount(), paySubmitRequest.getPayAmount());
            throw new BusinessException(ErrorEnum.PAY_AMOUNT_ERROR);
        }

        Money totalAmount = new Money(0);
        for (PayDetailRequest detail : paySubmitRequest.getPayDetailInfoList()) {
            totalAmount.addTo(new Money(detail.getPayAmount()));
        }

        if (!totalAmount.equals(new Money(paySubmitRequest.getPayAmount()))) {
            log.error("支付详情金额[{}]与支付总金额[{}]不匹配", totalAmount, paySubmitRequest.getPayAmount());
            throw new BusinessException(ErrorEnum.PAY_AMOUNT_ERROR);
        }
    }
}
