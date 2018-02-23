package com.dream.pay.center.core.order.handler;

import com.dream.pay.center.api.request.UnifiedOrderCreateRequest;
import com.dream.pay.center.api.response.UnifiedOrderCreateResult;
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
public class UnifiedOrderCreateHandler extends
        TransactionAbstractHandler<UnifiedOrderCreateRequest, UnifiedOrderCreateResult> {

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
    protected UnifiedOrderCreateResult execute(UnifiedOrderCreateRequest unifiedOrderCreateRequest) {
        //初始化执行上下文
        initContext(OperatorEnum.UNIFIED_ORDER_CREATE);
        return buildOrderCreatingResult(storeTradeInfo(unifiedOrderCreateRequest));
    }

    //创建新收单
    protected FundsTradeInfoEntity storeTradeInfo(UnifiedOrderCreateRequest unifiedOrderCreateRequest) {
        return wrapWithTransaction(status -> {
            FundsTradeInfoEntity fundsTradeInfoEntity;
            //幂等性校验(主键唯一)
            try {
                idempotencyComponent
                        .idempotency(unifiedOrderCreateRequest.getMerchantNo() + "-" + unifiedOrderCreateRequest.getBizTradeNo(),
                                OperatorEnum.UNIFIED_ORDER_CREATE);
            } catch (ComponentException e) {
                log.error("重复创建收单，执行异常,request={}", unifiedOrderCreateRequest);
                fundsTradeInfoEntity = fundsTradeInfoDao.selectByBizNoAndMerchantNo(unifiedOrderCreateRequest.getBizTradeNo(), unifiedOrderCreateRequest.getMerchantNo());
                return fundsTradeInfoEntity;
            }
            //构造单据
            fundsTradeInfoEntity = composeTradeInfo(unifiedOrderCreateRequest);
            //入库
            fundsTradeInfoDao.insert(fundsTradeInfoEntity);
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
    private UnifiedOrderCreateResult buildOrderCreatingResult(FundsTradeInfoEntity fundsTradeInfoEntity) {
        UnifiedOrderCreateResult unifiedOrderCreatingResult = new UnifiedOrderCreateResult();
        unifiedOrderCreatingResult.setPayTradeNo(fundsTradeInfoEntity.getPayTradeNo());
        unifiedOrderCreatingResult.setBizTradeNo(fundsTradeInfoEntity.getBizTradeNo());
        unifiedOrderCreatingResult.setTradeStatus(FundsTradeStatus.getByCode(fundsTradeInfoEntity.getTradeState()).getDescription());
        unifiedOrderCreatingResult.setTradeAmount(fundsTradeInfoEntity.getTradeAmount());
        return unifiedOrderCreatingResult;
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
                .expiredTime(DateUtil.StringToDefaultDate(request.getExpiredTime()))
                .extraInfo(request.getExtendInfo())
                .outBizContext(request.getTraceContext().getTraceId())
                .createTime(new Date())
                .updateTime(new Date()).build();
        return fundsTradeInfoEntity;
    }
}
