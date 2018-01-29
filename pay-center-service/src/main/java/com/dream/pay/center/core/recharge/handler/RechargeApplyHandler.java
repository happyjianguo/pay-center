package com.dream.pay.center.core.recharge.handler;

import com.dream.pay.center.api.request.RechargeApplyRequest;
import com.dream.pay.center.api.request.UnifiedOrderCreateRequest;
import com.dream.pay.center.api.response.RechargeApplyResult;
import com.dream.pay.center.common.enums.OperatorEnum;
import com.dream.pay.center.common.exception.ComponentException;
import com.dream.pay.center.component.IdempotencyComponent;
import com.dream.pay.center.dao.FundsTradeInfoDao;
import com.dream.pay.center.model.FundsTradeInfoEntity;
import com.dream.pay.center.service.context.FundsBaseContext;
import com.dream.pay.center.service.context.FundsBaseContextHolder;
import com.dream.pay.center.service.handler.TransactionAbstractHandler;
import com.dream.pay.center.service.sequence.SerialidGenerator;
import com.dream.pay.center.status.FundsTradeNotifyStatus;
import com.dream.pay.center.status.FundsTradeSettleStatus;
import com.dream.pay.center.status.FundsTradeStatus;
import com.dream.pay.utils.DateUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;

/**
 * 创建收单处理器.
 * <p>
 * 如果收单已经存在，则返回收单信息，否则，创建新的收单。
 *
 * @author mengzhenbin
 * @version UnifiedOrderCreatingHandler.java
 *          2017-01-11 13:21
 */
@Component
@Data
@Slf4j
public class RechargeApplyHandler extends
        TransactionAbstractHandler<RechargeApplyRequest, RechargeApplyResult> {

    //唯一性校验服务
    @Resource
    protected IdempotencyComponent idempotencyComponent;
    //收单－存储服务
    @Resource
    private FundsTradeInfoDao fundsTradeInfoDao;
    @Resource
    protected SerialidGenerator serialidGenerator;

    /**
     * {@inheritDoc}
     */
    @Override
    protected RechargeApplyResult execute(RechargeApplyRequest rechargeApplyRequest) {
        //初始化执行上下文
        initContext(OperatorEnum.UNIFIED_RECHARGE_APPLY);
        return buildOrderCreatingResult(storeTradeInfo(rechargeApplyRequest));
    }

    //创建新收单
    protected FundsTradeInfoEntity storeTradeInfo(RechargeApplyRequest rechargeApplyRequest) {
        return wrapWithTransaction(status -> {
            FundsTradeInfoEntity fundsTradeInfoEntity;
            //幂等性校验(主键唯一)
            try {
                idempotencyComponent
                        .idempotency(rechargeApplyRequest.getMerchantNo() + "-" + rechargeApplyRequest.getBizTradeNo(),
                                OperatorEnum.UNIFIED_RECHARGE_APPLY);
            } catch (ComponentException e) {
                log.error("重复充值申请，执行异常,request={}", rechargeApplyRequest);
                fundsTradeInfoEntity = fundsTradeInfoDao.selectByBizNoAndMerchantNo(rechargeApplyRequest.getBizTradeNo(), rechargeApplyRequest.getMerchantNo());
                return fundsTradeInfoEntity;
            }
            //构造单据
            fundsTradeInfoEntity = composeTradeInfo(rechargeApplyRequest);
            fundsTradeInfoDao.insert(fundsTradeInfoEntity);
            //TODO
            return fundsTradeInfoEntity;
        });

    }


    /**
     * 初始化上下文
     *
     * @param operatorEnum 操作类型
     */
    protected void initContext(OperatorEnum operatorEnum) {
        FundsBaseContext fundsBaseContext = new FundsBaseContext();
        fundsBaseContext.setOperatorEnum(operatorEnum);
        FundsBaseContextHolder.set(fundsBaseContext);
    }

    /**
     * 组装返回结果
     *
     * @param fundsTradeInfoEntity
     * @return DataResult<UnifiedOrderCreateResult>
     */
    private RechargeApplyResult buildOrderCreatingResult(FundsTradeInfoEntity fundsTradeInfoEntity) {
        RechargeApplyResult rechargeApplyResult = new RechargeApplyResult();
        rechargeApplyResult.setPayTradeNo(fundsTradeInfoEntity.getPayTradeNo());
        rechargeApplyResult.setBizTradeNo(fundsTradeInfoEntity.getBizTradeNo());
        rechargeApplyResult.setTradeStatus(FundsTradeStatus.getByCode(fundsTradeInfoEntity.getTradeState()).getDescription());
        rechargeApplyResult.setTradeAmount(fundsTradeInfoEntity.getTradeAmount());
        return rechargeApplyResult;
    }

    /**
     * 组装业务单数据
     *
     * @param request
     * @return FundsTradeInfoEntity
     */
    private FundsTradeInfoEntity composeTradeInfo(UnifiedOrderCreateRequest request) {
        FundsTradeInfoEntity fundsTradeInfoEntity = FundsTradeInfoEntity.builder()
                .payTradeNo(serialidGenerator.get())
                .bizTradeNo(request.getBizTradeNo())
                .bizTradeName(request.getTradeDesc())
                .bizProd(request.getBizProd())
                .bizMode(request.getBizMode())
                .bizAction(request.getBizAction())
                .merchantNo(Long.valueOf(request.getMerchantNo()))
                .userNo(request.getUserNo())
                .userName(request.getUserName())
                .partnerId(request.getPartnerId())
                .currency(Integer.valueOf(request.getCurrencyCode()))
                .tradeAmount(request.getTradeAmount())
                .tradeState(FundsTradeStatus.CREATE.getCode())
                .settledState(FundsTradeSettleStatus.CREATE.getCode())
                .notifyState(FundsTradeNotifyStatus.NONE.getCode())
                .expiredTime(DateUtil.StringToDateTime(request.getExpiredTime()))
                .extraInfo(request.getExtendInfo())
                .outBizContext(request.getTraceContext().getTraceId())
                .createTime(new Date())
                .updateTime(new Date()).build();
        return fundsTradeInfoEntity;
    }
}
