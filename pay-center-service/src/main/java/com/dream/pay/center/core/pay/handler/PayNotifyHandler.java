package com.dream.pay.center.core.pay.handler;

import com.dream.center.out.mock.dto.OperationStatusEnum;
import com.dream.center.out.mock.dto.PayOperationResult;
import com.dream.pay.bean.Money;
import com.dream.pay.center.api.message.PayNsqMessage;
import com.dream.pay.center.common.enums.OperatorEnum;
import com.dream.pay.center.common.enums.PayTagEnum;
import com.dream.pay.center.component.AbnormalPayComponent;
import com.dream.pay.center.core.pay.service.PayCoreService;
import com.dream.pay.center.dao.FundsPayDetailDao;
import com.dream.pay.center.dao.FundsTradeInfoDao;
import com.dream.pay.center.dao.FundsTradeItemsDao;
import com.dream.pay.center.model.FundsPayDetailEntity;
import com.dream.pay.center.model.FundsTradeInfoEntity;
import com.dream.pay.center.model.FundsTradeItemsEntity;
import com.dream.pay.center.service.context.FundsBaseContextHolder;
import com.dream.pay.center.service.context.FundsPayContext;
import com.dream.pay.center.service.handler.AbstractHandler;
import com.dream.pay.center.status.FundsPayStatus;
import com.dream.pay.utils.JsonUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import javax.annotation.Resource;
import java.util.List;

/**
 * 支付通知处理器.
 * <p>
 *
 * @author mengzhenbin
 * @version PayNotifyHandler.java
 *          2017-01-11 13:21
 */
@Component
@Data
@Slf4j
public class PayNotifyHandler extends
        AbstractHandler<PayOperationResult, Boolean> {

    @Resource
    protected TransactionTemplate transactionTemplate;

    //收单－存储服务
    @Resource
    private FundsTradeInfoDao fundsTradeInfoDao;
    @Resource
    private FundsTradeItemsDao fundsTradeItemsDao;
    @Resource
    private FundsPayDetailDao fundsPayDetailDao;
    @Resource
    private AbnormalPayComponent abnormalPayComponent;

    //支付核心接口
    @Resource
    protected PayCoreService payCoreService;

    @Value("${nsq.pay.messgae.producer.topic}")
    private String topic;


    @Override
    protected Boolean execute(PayOperationResult payResult) {

        //初始化执行上下文
        initContext(OperatorEnum.UNIFORM_PAYMENT);

        //1.参数校验不通过就返回
        if (!paramValidate(payResult)) {
            return Boolean.FALSE;
        }
        //2.开本地事务
        doTransaction(payResult);

        return Boolean.TRUE;
    }

    private Boolean doTransaction(PayOperationResult payResult) {
        return transactionTemplate
                .execute(new TransactionCallback<Boolean>() {
                    @Override
                    public Boolean doInTransaction(TransactionStatus status) {
                        FundsPayContext fundsPayContext = (FundsPayContext) FundsBaseContextHolder.get();

                        //查询当前支付详情单信息
                        FundsPayDetailEntity payCurrentDetail = fundsPayDetailDao.loadByDetailNo(payResult.getPayDetailNo());
                        fundsPayContext.setCurrentDetail(payCurrentDetail);

                        //判断是否重复通知
                        if (checkRepeatNotice(payCurrentDetail)) {
                            return Boolean.TRUE;
                        }

                        //查询业务单
                        FundsTradeInfoEntity fundsTradeInfoEntity = fundsTradeInfoDao.loadByPayTradeNo(payCurrentDetail.getPayTradeNo());
                        fundsPayContext.setFundsTradeInfoEntity(fundsTradeInfoEntity);

                        //查询支付总单
                        FundsTradeItemsEntity fundsTradeItemsEntity = fundsTradeItemsDao.loadByItemNo(payCurrentDetail.getPayTradeItemsNo());
                        fundsPayContext.setFundsTradeItemsEntity(fundsTradeItemsEntity);

                        //查询支付详情单
                        List<FundsPayDetailEntity> payDetailList = fundsPayDetailDao.loadByItemNo(payCurrentDetail.getPayTradeItemsNo());
                        fundsPayContext.setFundsPayDetailEntitys(payDetailList);

                        //查询支付业务单下所有的支付子单信息
                        List<FundsTradeItemsEntity> tradeItems = fundsTradeItemsDao.loadByPayTradeNo(payCurrentDetail.getPayTradeNo());

                        //判断是否重复支付／多付／少付
                        PayTagEnum payTag = checkPayLegality(tradeItems, fundsTradeItemsEntity, payCurrentDetail, payResult);
                        fundsPayContext.setPayTag(payTag);

                        //调用支付确认组件处理
                        fundsPayContext.setPreDetailStatus(payResult.getOperateResultCode());
                        payCoreService.confirmPay(fundsPayContext, payResult);

                        if (OperationStatusEnum.SUCCESS.equals(fundsPayContext.getPreDetailStatus())) {
                            PayNsqMessage payNsqMessage = composePayNsqMessage();
                            //成功需要开启分布式事务发送记账和结算消息
                            send(topic, payCurrentDetail.getPayDetailNo() + "_" + FundsPayStatus.getByCode(payCurrentDetail.getPayStatus()), JsonUtil.toJson(payNsqMessage));
                        }

                        if (null != payTag) { //如果是异常支付单，加到异常表里。
                            abnormalPayComponent.save(payCurrentDetail, payTag);
                        }

                        return Boolean.TRUE;
                    }
                });
    }

    /**
     * 创建支付消息
     *
     * @return
     */
    private PayNsqMessage composePayNsqMessage() {
        FundsPayContext fundsPayContext = (FundsPayContext) FundsBaseContextHolder.get();
        FundsTradeInfoEntity fundsTradeInfoEntity = fundsPayContext.getFundsTradeInfoEntity();
        FundsTradeItemsEntity fundsTradeItemsEntity = fundsPayContext.getFundsTradeItemsEntity();
        FundsPayDetailEntity payCurrentDetail = fundsPayContext.getCurrentDetail();
        PayNsqMessage payNsqMessage = new PayNsqMessage();
        payNsqMessage.setBizProd(String.valueOf(fundsTradeInfoEntity.getBizProd()));
        payNsqMessage.setBizMode(String.valueOf(fundsTradeInfoEntity.getBizMode()));
        payNsqMessage.setBizAction(String.valueOf(fundsTradeItemsEntity.getBizAction()));
        payNsqMessage.setBizSubAction(String.valueOf(payCurrentDetail.getBizSubAction()));
        payNsqMessage.setPayChannel(payCurrentDetail.getPayTool());
        payNsqMessage.setOutBizNo(fundsTradeInfoEntity.getBizTradeNo());
        payNsqMessage.setAcquireNo(fundsTradeInfoEntity.getPayTradeNo());
        payNsqMessage.setPayDetailNo(payCurrentDetail.getPayDetailNo());
        payNsqMessage.setMerchantNo(String.valueOf(fundsTradeInfoEntity.getMerchantNo()));
        payNsqMessage.setPartnerId(fundsTradeInfoEntity.getPartnerId());
        payNsqMessage.setPayStatus(String.valueOf(payCurrentDetail.getPayStatus()));
        payNsqMessage.setPayAmount(payCurrentDetail.getPayAmount());
        payNsqMessage.setCurrency(payCurrentDetail.getCurrency());
        payNsqMessage.setPayCreateTime(payCurrentDetail.getCreateTime());
        payNsqMessage.setPayFinishedTime(payCurrentDetail.getOutFinishTime());
        payNsqMessage.setPayTool(payCurrentDetail.getPayTool());
        payNsqMessage.setChannelSettleNo(payCurrentDetail.getChannelReturnNo());
        payNsqMessage.setPayTag(payCurrentDetail.getPayTag());
        payNsqMessage.setTradeDesc(fundsTradeInfoEntity.getBizTradeName());
        return payNsqMessage;
    }


    /**
     * 判断是否为重复通知
     *
     * @param payCurrentDetail
     */
    private boolean checkRepeatNotice(FundsPayDetailEntity payCurrentDetail) {
        if (FundsPayStatus.SUCCESS.getCode().equals(payCurrentDetail.getPayStatus())) {
            log.info("[重复通知]-{}-支付成功消息已处理", payCurrentDetail.getPayDetailNo());
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

    /**
     * 判断是否重复支付,少付，多付
     *
     * @param fundsTradeItemsEntitys
     * @param currentItem
     * @param fundsPayDetailEntity
     * @param payResult
     * @return PayTagEnum
     */
    private PayTagEnum checkPayLegality(List<FundsTradeItemsEntity> fundsTradeItemsEntitys, FundsTradeItemsEntity currentItem, FundsPayDetailEntity fundsPayDetailEntity, PayOperationResult payResult) {
        PayTagEnum payTag = null;
        //1.判断重复支付，标记重复支付
        for (FundsTradeItemsEntity fundsTradeItemsEntity : fundsTradeItemsEntitys) {
            if (fundsTradeItemsEntity != null && FundsPayStatus.SUCCESS.getCode().equals(fundsTradeItemsEntity.getTradeState()) && !fundsTradeItemsEntity.equals(currentItem)) {
                payTag = PayTagEnum.REPEATED_PAY;
                break;
            }
        }
        //2.判断金额是否一致,标记少付／超付
        if (payTag == null && fundsPayDetailEntity.getPayAmount() != payResult.getRealAmount()) {
            payTag = new Money(fundsPayDetailEntity.getPayAmount()).greaterThan(new Money(payResult.getRealAmount())) ? PayTagEnum.LESS_PAY : PayTagEnum.OVER_PAY;
        }
        return payTag;
    }


    /**
     * 校验参数
     *
     * @param payResult
     * @return
     */
    private boolean paramValidate(PayOperationResult payResult) {
        if (payResult == null) {
            log.warn("支付回调,入参数为空");
            return false;
        }
        //未达终态
        if (!payResult.getOperateResultCode().equals(OperationStatusEnum.SUCCESS)) {
            log.warn("支付回调,未到达终态,resultCode()={}", payResult.getOperateResultCode());
            return false;
        }

        if (StringUtils.isBlank(payResult.getPayChannelNo()) || StringUtils.isBlank(payResult.getPayDetailNo())) {
            log.warn("支付回调,渠道流水号或支付明细号不能为空,渠道流水号=[{}],支付明细号=[{}]", payResult.getPayChannelNo(), payResult.getPayDetailNo());
            return false;
        }
        return true;
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
}


