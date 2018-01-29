package com.dream.pay.center.core.refund.handler;

import com.dream.center.out.mock.dto.OperationStatusEnum;
import com.dream.pay.bean.Money;
import com.dream.pay.center.api.request.RefundApplyRequest;
import com.dream.pay.center.api.response.RefundApplyResult;
import com.dream.pay.center.common.enums.ErrorEnum;
import com.dream.pay.center.common.enums.OperatorEnum;
import com.dream.pay.center.common.enums.RefundMode;
import com.dream.pay.center.common.exception.BusinessException;
import com.dream.pay.center.common.exception.ComponentException;
import com.dream.pay.center.common.exception.RepositoryException;
import com.dream.pay.center.component.IdempotencyComponent;
import com.dream.pay.center.core.refund.enums.RefundStatusEnum;
import com.dream.pay.center.core.refund.enums.RefundTaskEnum;
import com.dream.pay.center.core.refund.fsm.RefundFSM;
import com.dream.pay.center.dao.*;
import com.dream.pay.center.model.*;
import com.dream.pay.center.service.context.FundsBaseContextHolder;
import com.dream.pay.center.service.context.FundsRefundContext;
import com.dream.pay.center.service.handler.TransactionAbstractHandler;
import com.dream.pay.center.service.sequence.SerialidGenerator;
import com.dream.pay.center.status.FundsJobStatus;
import com.dream.pay.center.status.FundsTradeItemStatus;
import com.dream.pay.center.status.FundsTradeStatus;
import com.dream.pay.enums.BizActionEnum;
import com.dream.pay.enums.InOutEnum;
import com.dream.pay.enums.PayTool;
import com.dream.pay.enums.UnifiedBizCode;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionStatus;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
@Data
@Slf4j
public class RefundApplyHandler extends
        TransactionAbstractHandler<RefundApplyRequest, RefundApplyResult> {
    //唯一性校验服务
    @Resource
    protected IdempotencyComponent idempotencyComponent;
    //收单－存储服务
    @Resource
    private FundsTradeInfoDao fundsTradeInfoDao;
    @Resource
    protected SerialidGenerator serialidGenerator;
    @Resource
    private FundsTradeItemsDao fundsTradeItemsDao;
    @Resource
    private FundsPayDetailDao fundsPayDetailDao;
    @Resource
    private FundsRefundDetailDao fundsRefundDetailDao;
    @Resource
    private FundsRefundJobDao fundsRefundJobDao;
    @Resource
    private RefundFSM refundFSM;

    @Value("${common.env}")
    private String env;


    @Override
    protected RefundApplyResult execute(RefundApplyRequest refundApplyRequest) {
        //初始化执行上下文
        initContext(OperatorEnum.UNIFIED_REFUND_APPLY);
        FundsRefundContext fundsRefundContext = (FundsRefundContext) FundsBaseContextHolder.get();

        RefundApplyResult refundApplyResult = wrapWithTransaction((TransactionStatus status) -> {
            try {
                //幂等性校验
                idempotencyComponent.idempotency(refundApplyRequest.getOutBizNo() + refundApplyRequest.getPayTradeNo(),
                        OperatorEnum.UNIFIED_REFUND_APPLY);

                //获取收单记录
                FundsTradeInfoEntity fundsTradeInfoEntity = fundsTradeInfoDao.selectByPayTradeNoForUpdate(refundApplyRequest.getPayTradeNo());
                fundsRefundContext.setFundsTradeInfoEntity(fundsTradeInfoEntity);

                //退款业务校验：包括，收单是否存在，金额是否合法，状态是否合法
                this.refundBizCheck(refundApplyRequest, fundsTradeInfoEntity);

                //增加退款中金额字段
                fundsTradeInfoDao.incRefundInProcessAmount(FundsTradeStatus.REFUND.getCode(), fundsTradeInfoEntity.getPayTradeNo(), refundApplyRequest.getRefundAmount());

                //构建并且存储退款总单模型
                FundsTradeItemsEntity fundsTradeItemsEntity = this.composeTradeItemInfo(fundsTradeInfoEntity, refundApplyRequest);
                fundsRefundContext.setFundsTradeItemsEntity(fundsTradeItemsEntity);
                fundsTradeItemsDao.insert(fundsTradeItemsEntity);

                //构建并且存储退款详情单模型
                buildRefundData(refundApplyRequest, fundsRefundContext, fundsTradeInfoEntity);

            } catch (ComponentException e) {
                log.error("[申请退款]-幂等异常,request={}", refundApplyRequest);
                FundsTradeItemsEntity fundsTradeItemsEntity = fundsTradeItemsDao.loadByBizTradeNo(refundApplyRequest.getOutBizNo(), refundApplyRequest.getPayTradeNo());
                //如果重复请求则直接返回当前退款单的相关信息
                return buildRefundApplyResult(fundsTradeItemsEntity, true);
            } catch (BusinessException e) {
                log.error("[申请退款]-业务异常", e);
                throw e;
            } catch (Exception e) {
                log.error("[申请退款]-数据存储异常", e);
                throw new RepositoryException(ErrorEnum.UN_KNOW_EXCEPTION);
            }
            return null;
        });

        //幂等直接返回
        if (null != refundApplyResult && refundApplyResult.isIdempotent()) {
            return refundApplyResult;
        }
        //调用账务冻结操作
        OperationStatusEnum accountResult = refundFSM.handler(fundsRefundContext.getFundsTradeItemsEntity(), null, null); //调用账务余额冻结
        if (OperationStatusEnum.FAIL.equals(accountResult))
            throw new BusinessException(ErrorEnum.REFUND_PRE_TRANS_OUT_ERROR);

        return buildRefundApplyResult(fundsRefundContext.getFundsTradeItemsEntity(), false);
    }

    /**
     * 组装退款明细单以及退款任务
     *
     * @param refundApplyRequest
     * @param fundsRefundContext
     * @param fundsTradeInfoEntity
     */
    private void buildRefundData(RefundApplyRequest refundApplyRequest, FundsRefundContext fundsRefundContext, FundsTradeInfoEntity fundsTradeInfoEntity) {
        Money refundingAmount = new Money(null != fundsTradeInfoEntity.getRefundInprocessAmount() ? fundsTradeInfoEntity.getRefundInprocessAmount() : 0);//退款中金额
        Money refundedAmount = new Money(null != fundsTradeInfoEntity.getRefundAmount() ? fundsTradeInfoEntity.getRefundAmount() : 0);//已退金额
        Money needToRefund = new Money(refundApplyRequest.getRefundAmount());//本次退款

        List<FundsPayDetailEntity> payDetailEntities = fundsPayDetailDao.loadForRefund(fundsTradeInfoEntity.getPayTradeNo());

        List<FundsRefundDetailEntity> fundsRefundDetails = new ArrayList<FundsRefundDetailEntity>();
        List<FundsRefundJobEntity> fundsRefundJobs = new ArrayList<FundsRefundJobEntity>();

        for (FundsPayDetailEntity fundsPayDetailEntity : payDetailEntities) {
            log.info("退款申请-还需要退款金额=[{}]", needToRefund);
            Money realPayAmount = new Money(fundsPayDetailEntity.getRealAmount());//实付金额
            log.info("退款申请-本次处理，对应正向单号[{}]-实付金额=[{}]", fundsPayDetailEntity.getPayDetailNo(), realPayAmount);
            if (realPayAmount.greaterThan(refundingAmount.addToWithNewObj(refundedAmount))) {  //当前单尚有可退金额

                Money canRefundAmount = realPayAmount.subToWithNewObj(refundingAmount).subToWithNewObj(refundedAmount);
                log.info("退款申请-本次处理，对应正向单号[{}]-仍有可退金额=[{}]", canRefundAmount);
                if (canRefundAmount.greaterOrEqualThan(needToRefund)) {
                    log.info("退款申请-本次处理，对应正向单号[{}]-可退金额[{}]>=需退金额[{}]", canRefundAmount, needToRefund);
                    //当前单满足本次退款金额，使用此正向单生成退款记录
                    FundsRefundDetailEntity fundsRefundDetailEntity = this.composeRefundDetail(fundsTradeInfoEntity, fundsPayDetailEntity, refundApplyRequest, needToRefund.getCent());
                    fundsRefundDetailDao.insert(fundsRefundDetailEntity);
                    fundsRefundDetails.add(fundsRefundDetailEntity);

                    FundsRefundJobEntity fundsRefundJobEntity = composeRefundJob(fundsRefundDetailEntity);
                    fundsRefundJobDao.insert(fundsRefundJobEntity);
                    fundsRefundJobs.add(fundsRefundJobEntity);
                    break;
                } else {
                    log.info("退款申请-本次处理，对应正向单号[{}]-可退金额[{}]<需退金额[{}]", canRefundAmount, needToRefund);
                    //当前单不满足本次退款金额，优先使用此正向单生成退款记录
                    FundsRefundDetailEntity fundsRefundDetailEntity = this.composeRefundDetail(fundsTradeInfoEntity, fundsPayDetailEntity, refundApplyRequest, canRefundAmount.getCent());
                    fundsRefundDetailDao.insert(fundsRefundDetailEntity);
                    fundsRefundDetails.add(fundsRefundDetailEntity);

                    FundsRefundJobEntity fundsRefundJobEntity = composeRefundJob(fundsRefundDetailEntity);
                    fundsRefundJobDao.insert(fundsRefundJobEntity);
                    fundsRefundJobs.add(fundsRefundJobEntity);

                    needToRefund = needToRefund.subToWithNewObj(canRefundAmount);

                    continue;
                }
            } else {
                log.info("退款申请-本次处理，对应正向单号[{}]-没有可退金额]");
                continue;
            }
        }
        fundsRefundContext.setFundsRefundDetails(fundsRefundDetails);
        fundsRefundContext.setFundsRefundJobs(fundsRefundJobs);
    }


    /**
     * 组装退款任务数据
     *
     * @param fundsRefundDetailEntity
     * @return FundsRefundJobEntity
     */
    private FundsRefundJobEntity composeRefundJob(FundsRefundDetailEntity fundsRefundDetailEntity) {
        FundsRefundJobEntity fundsRefundJobEntity = FundsRefundJobEntity.builder()
                .refundDetailNo(fundsRefundDetailEntity.getRefundDetailNo())
                .payTradeItemsNo(fundsRefundDetailEntity.getPayTradeItemsNo())
                .payTradeNo(fundsRefundDetailEntity.getPayTradeNo())
                .jobType(RefundTaskEnum.INVOKE_ACCOUNT_TRANS_OUT.getCode())
                .jobTypeDesc(RefundTaskEnum.INVOKE_ACCOUNT_TRANS_OUT.getDesc())
                .jobStatus(FundsJobStatus.TODO.getCode())
                .jobRunCount(0)
                .env(env)
                .createTime(new Date())
                .updateTime(new Date()).build();
        return fundsRefundJobEntity;
    }


    /**
     * 组装退款详情单数据
     *
     * @param fundsPayDetailEntity
     * @param refundApplyRequest
     * @param refundAmount
     * @return FundsRefundDetailEntity
     */
    private FundsRefundDetailEntity composeRefundDetail(FundsTradeInfoEntity fundsTradeInfoEntity, FundsPayDetailEntity fundsPayDetailEntity, RefundApplyRequest refundApplyRequest, Long refundAmount) {
        FundsRefundDetailEntity fundsRefundDetailEntity = FundsRefundDetailEntity.builder()
                .refundDetailNo(serialidGenerator.get())
                .bizTradeNo(fundsPayDetailEntity.getBizTradeNo())
                .payTradeNo(fundsPayDetailEntity.getPayTradeNo())
                .payTradeItemsNo(fundsPayDetailEntity.getPayTradeItemsNo())
                .currency(Integer.valueOf(fundsPayDetailEntity.getCurrency()))
                .refundAmount(refundAmount)
                .refundStatus(RefundStatusEnum.APPLYING.getStatus())
                .merchantNo(String.valueOf(fundsPayDetailEntity.getMerchantNo()))
                .refundMode(refundApplyRequest.getRefundMode())
                .payAmount(fundsPayDetailEntity.getPayAmount())
                .payDetailNo(fundsPayDetailEntity.getPayDetailNo())
                .channelPayDetailNo(fundsPayDetailEntity.getChannelReturnNo())
                .payTool(fundsPayDetailEntity.getPayTool())
                .refundNote(buildRefundNote(fundsTradeInfoEntity, fundsPayDetailEntity))
                .createTime(new Date())
                .updateTime(new Date()).build();
        return fundsRefundDetailEntity;
    }

    /**
     * 封装四码一号
     *
     * @param fundsPayDetailEntity
     * @param fundsTradeInfoEntity
     * @return
     */
    private String buildRefundNote(FundsTradeInfoEntity fundsTradeInfoEntity, FundsPayDetailEntity fundsPayDetailEntity) {
        UnifiedBizCode unifiedBizCode = new UnifiedBizCode();
        unifiedBizCode.setBizProdCode(String.valueOf(fundsTradeInfoEntity.getBizProd()));//产品码
        unifiedBizCode.setBizModeCode(String.valueOf(fundsTradeInfoEntity.getBizMode()));//业务模式码
        unifiedBizCode.setBizActionCode(String.valueOf(BizActionEnum.REFUND.getCode()));//业务流向码
        unifiedBizCode.setBizPayToolCode(String.valueOf(PayTool.selectByName(fundsPayDetailEntity.getPayTool()).getBizSubAction()));//业务流向子码
        unifiedBizCode.setBizChannelCode(String.valueOf(fundsPayDetailEntity.getBizChannel()));//渠道号
        return unifiedBizCode.getFullCode();
    }

    /**
     * 组装业务子单数据
     *
     * @param fundsTradeInfoEntity
     * @param refundApplyRequest
     * @return FundsTradeItemsEntity
     */
    private FundsTradeItemsEntity composeTradeItemInfo(FundsTradeInfoEntity fundsTradeInfoEntity, RefundApplyRequest refundApplyRequest) {
        FundsTradeItemsEntity fundsTradeItemsEntity = FundsTradeItemsEntity.builder()
                .payTradeItemsNo(serialidGenerator.get())
                .payTradeNo(fundsTradeInfoEntity.getPayTradeNo())
                .bizTradeNo(refundApplyRequest.getOutBizNo())
                .bizAction(BizActionEnum.REFUND.getCode())
                .fundsInOut(InOutEnum.OUT.getValue())
                .mode(refundApplyRequest.getRefundMode())
                .tradeAmount(refundApplyRequest.getRefundAmount())
                .merchantNo(fundsTradeInfoEntity.getMerchantNo())
                .tradeState(FundsTradeItemStatus.DOING.getCode())
                .tradeNote(RefundMode.getByCode(refundApplyRequest.getRefundMode()).getDesc())
                .createTime(new Date())
                .updateTime(new Date()).build();
        return fundsTradeItemsEntity;
    }


    /**
     * 退款业务校验：包括，收单是否存在，金额是否合法，状态是否合法
     *
     * @param refundApplyRequest
     * @param fundsTradeInfoEntity
     */
    private void refundBizCheck(RefundApplyRequest refundApplyRequest, FundsTradeInfoEntity fundsTradeInfoEntity) {
        if (fundsTradeInfoEntity == null) {
            log.error("退款申请-业务校验-[支付单不存在]-[{}]", fundsTradeInfoEntity);
            throw new BusinessException(ErrorEnum.PAY_ORDER_NOT_EXIST);
        }

        if (!FundsTradeStatus.isCanRefundStatus(FundsTradeStatus.getByCode(fundsTradeInfoEntity.getTradeState()))) {
            log.error("退款申请-业务校验-[支付单状态暂不支持退款操作]-[{}]", fundsTradeInfoEntity.getTradeState());
            throw new BusinessException(ErrorEnum.PAY_STATUS_INVALID);
        }

        if (!isCanRefundAmount(refundApplyRequest, fundsTradeInfoEntity)) {
            log.error("退款申请-业务校验-[退款金额非法或已超退]-已退金额=[{}]，退款中金额=[{}],本次申请退款金额=[{}]",
                    fundsTradeInfoEntity.getRefundAmount(), fundsTradeInfoEntity.getRefundInprocessAmount(), refundApplyRequest.getRefundAmount());
            throw new BusinessException(ErrorEnum.REFUND_AMOUNT_INVALID);
        }
    }

    /**
     * 判断金额是否可退
     *
     * @param refundApplyRequest
     * @param fundsTradeInfoEntity
     * @return boolean
     */
    private boolean isCanRefundAmount(RefundApplyRequest refundApplyRequest, FundsTradeInfoEntity fundsTradeInfoEntity) {
        if (null == fundsTradeInfoEntity.getRealAmount()) return false;
        return new Money(fundsTradeInfoEntity.getRealAmount())
                .greaterOrEqualThan(
                        new Money(refundApplyRequest.getRefundAmount())
                                .addToWithNewObj(new Money(null != fundsTradeInfoEntity.getRefundInprocessAmount() ? fundsTradeInfoEntity.getRefundInprocessAmount() : 0)//退款中金额
                                        .addToWithNewObj(new Money(null != fundsTradeInfoEntity.getRefundAmount() ? fundsTradeInfoEntity.getRefundAmount() : 0))));//已退金额
    }

    /**
     * 构造返回结果
     *
     * @param fundsTradeItemsEntity
     * @param isIdempotent
     * @return
     */
    private RefundApplyResult buildRefundApplyResult(FundsTradeItemsEntity fundsTradeItemsEntity, Boolean isIdempotent) {
        RefundApplyResult refundApplyResult = new RefundApplyResult();
        refundApplyResult.setIdempotent(isIdempotent);
        refundApplyResult.setBizTradeNo(fundsTradeItemsEntity.getBizTradeNo());
        refundApplyResult.setPayTradeNo(fundsTradeItemsEntity.getPayTradeNo());
        refundApplyResult.setRefundStatus(FundsTradeItemStatus.buildRefundStatus(FundsTradeItemStatus.getByCode(fundsTradeItemsEntity.getTradeState())));
        return refundApplyResult;
    }

    /**
     * 初始化上下文
     *
     * @param operatorEnum 操作类型
     */
    protected void initContext(OperatorEnum operatorEnum) {
        FundsRefundContext fundsRefundContext = new FundsRefundContext();
        fundsRefundContext.setOperatorEnum(operatorEnum);
        FundsBaseContextHolder.set(fundsRefundContext);
    }
}