package com.dream.pay.center.core.withdraw.handler;


import com.dream.center.out.mock.dto.BankCardInfoResult;
import com.dream.center.out.mock.dto.OperationStatusEnum;
import com.dream.pay.center.api.request.WithdrawApplyRequest;
import com.dream.pay.center.api.response.WithdrawApplyResult;
import com.dream.pay.center.common.enums.ErrorEnum;
import com.dream.pay.center.common.enums.OperatorEnum;
import com.dream.pay.center.common.exception.BaseException;
import com.dream.pay.center.common.exception.BusinessException;
import com.dream.pay.center.common.exception.ComponentException;
import com.dream.pay.center.common.exception.RepositoryException;
import com.dream.pay.center.component.IdempotencyComponent;
import com.dream.pay.center.core.withdraw.enums.WithdrawStatusEnum;
import com.dream.pay.center.core.withdraw.enums.WithdrawTaskEnum;
import com.dream.pay.center.core.withdraw.fsm.WithdrawFSM;
import com.dream.pay.center.dao.FundsTradeInfoDao;
import com.dream.pay.center.dao.FundsTradeItemsDao;
import com.dream.pay.center.dao.FundsWithdrawDetailDao;
import com.dream.pay.center.dao.FundsWithdrawJobDao;
import com.dream.pay.center.model.FundsTradeInfoEntity;
import com.dream.pay.center.model.FundsTradeItemsEntity;
import com.dream.pay.center.model.FundsWithdrawDetailEntity;
import com.dream.pay.center.model.FundsWithdrawJobEntity;
import com.dream.pay.center.service.context.FundsBaseContextHolder;
import com.dream.pay.center.service.context.FundsWithdrawContext;
import com.dream.pay.center.service.handler.TransactionAbstractHandler;
import com.dream.pay.center.service.out.MerchantService;
import com.dream.pay.center.service.out.RiskService;
import com.dream.pay.center.service.sequence.SerialidGenerator;
import com.dream.pay.center.status.FundsJobStatus;
import com.dream.pay.center.status.FundsTradeNotifyStatus;
import com.dream.pay.center.status.FundsTradeSettleStatus;
import com.dream.pay.center.status.FundsTradeStatus;
import com.dream.pay.constants.UnifiedBizCode;
import com.dream.pay.enums.BizActionEnum;
import com.dream.pay.enums.BizModeEnum;
import com.dream.pay.enums.InOutEnum;
import com.dream.pay.enums.PayTool;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;

@Component
@Data
@Slf4j
public class WithdrawApplyHandler extends
        TransactionAbstractHandler<WithdrawApplyRequest, WithdrawApplyResult> {

    //唯一性校验服务
    @Resource
    protected IdempotencyComponent idempotencyComponent;
    @Resource
    protected SerialidGenerator serialidGenerator;
    //收单－存储服务
    @Resource
    private FundsTradeInfoDao fundsTradeInfoDao;
    @Resource
    private FundsTradeItemsDao fundsTradeItemsDao;
    @Resource
    private FundsWithdrawDetailDao fundsWithdrawDetailDao;
    @Resource
    private FundsWithdrawJobDao fundsWithdrawJobDao;

    @Resource
    private MerchantService merchantService;
    @Resource
    private RiskService riskService;

    @Value("${common.env}")
    private String env;

    @Resource
    private WithdrawFSM withdrawFSM;

    @Override
    protected WithdrawApplyResult execute(WithdrawApplyRequest withdrawApplyRequest) {

        //初始化执行上下文
        initContext(OperatorEnum.UNIFIED_WITHDRAW_APPLY);

        //获取银行卡信息
        BankCardInfoResult bankCardInfo = merchantService.queryCardInfo(withdrawApplyRequest.getMerchantNo(), withdrawApplyRequest.getCardBindId(), BizActionEnum.WITHDRAW.getCode());

        //风控黑白名单检查
        riskService.riskCheck(withdrawApplyRequest.getMerchantNo());

        //额度检查

        //生成提现单
        buildFundsWithdrawContext(withdrawApplyRequest, bankCardInfo);

        //开事务存储提现单
        return executeWithdrawApply(withdrawApplyRequest);
    }

    private void buildFundsWithdrawContext(WithdrawApplyRequest withdrawApplyRequest, BankCardInfoResult bankCardInfo) {

        FundsWithdrawContext fundsWithdrawContext = (FundsWithdrawContext) FundsBaseContextHolder.get();
        //创建收单信息
        FundsTradeInfoEntity fundsTradeInfoEntity = composeTradeInfo(withdrawApplyRequest);
        fundsWithdrawContext.setFundsTradeInfoEntity(fundsTradeInfoEntity);

        //创建子收单信息
        FundsTradeItemsEntity fundsTradeItemsEntity = composeTradeItemInfo(fundsTradeInfoEntity);
        fundsWithdrawContext.setFundsTradeItemsEntity(fundsTradeItemsEntity);

        //创建提现详情信息
        FundsWithdrawDetailEntity withdrawDetailEntity = composeWithdrawDetail(fundsTradeInfoEntity, fundsTradeItemsEntity, withdrawApplyRequest, bankCardInfo);
        fundsWithdrawContext.setFundsWithdrawDetailEntity(withdrawDetailEntity);

        FundsWithdrawJobEntity fundsWithdrawJobEntity = composeWithdrawJob(fundsWithdrawContext.getFundsWithdrawDetailEntity(), fundsWithdrawContext.getFundsTradeItemsEntity());
        fundsWithdrawContext.setFundsWithdrawJobEntity(fundsWithdrawJobEntity);

    }

    protected WithdrawApplyResult executeWithdrawApply(WithdrawApplyRequest withdrawApplyRequest) throws BaseException {
        FundsWithdrawContext fundsWithdrawContext = (FundsWithdrawContext) FundsBaseContextHolder.get();
        WithdrawApplyResult withdrawApplyResult = wrapWithTransaction(status -> {
            try {
                //幂等性校验
                idempotencyComponent.idempotency(withdrawApplyRequest.getOutBizNo() + withdrawApplyRequest.getMerchantNo(),
                        OperatorEnum.UNIFIED_WITHDRAW_APPLY);
                fundsTradeInfoDao.insert(fundsWithdrawContext.getFundsTradeInfoEntity());
                fundsTradeItemsDao.insert(fundsWithdrawContext.getFundsTradeItemsEntity());
                fundsWithdrawDetailDao.insert(fundsWithdrawContext.getFundsWithdrawDetailEntity());
                fundsWithdrawJobDao.insert(fundsWithdrawContext.getFundsWithdrawJobEntity());

            } catch (ComponentException e) {
                log.error("[申请提现]-幂等异常,request={}", withdrawApplyRequest);
                FundsTradeInfoEntity fundsTradeInfoEntity = fundsTradeInfoDao.selectByBizNoAndMerchantNo(withdrawApplyRequest.getOutBizNo(), withdrawApplyRequest.getMerchantNo());
                status.setRollbackOnly();
                //组装当前提现单状态并且返回
                return buildWithdrawApplyResult(fundsTradeInfoEntity, true);
            } catch (BusinessException e) {
                log.error("[申请提现]-业务异常", e);
                throw e;
            } catch (Exception e) {
                status.setRollbackOnly();
                log.error("[申请提现]-数据存储异常", e);
                throw new RepositoryException(ErrorEnum.UN_KNOW_EXCEPTION);
            }
            return null;
        });

        //幂等直接返回
        if (null != withdrawApplyResult && withdrawApplyResult.isIdempotent()) {
            return withdrawApplyResult;
        }
        //调用账务余额冻结
        OperationStatusEnum accountResult = withdrawFSM.handler(fundsWithdrawContext.getFundsWithdrawDetailEntity(), fundsWithdrawContext.getFundsWithdrawJobEntity());

        if (OperationStatusEnum.FAIL.equals(accountResult)) {
            throw new BusinessException(ErrorEnum.WITHDRAW_PRE_TRANS_OUT_ERROR);
        }

        return buildWithdrawApplyResult(fundsWithdrawContext.getFundsTradeInfoEntity(), false);
    }

    /**
     * 组装提现任务数据
     *
     * @param withdrawDetailEntity
     * @param fundsTradeItemsEntity
     * @return FundsWithdrawJobEntity
     */
    private FundsWithdrawJobEntity composeWithdrawJob(FundsWithdrawDetailEntity withdrawDetailEntity, FundsTradeItemsEntity fundsTradeItemsEntity) {
        FundsWithdrawJobEntity fundsWithdrawJobEntity = new FundsWithdrawJobEntity();
        fundsWithdrawJobEntity.setWithdrawDetailNo(withdrawDetailEntity.getWithdrawDetailNo());
        fundsWithdrawJobEntity.setPayTradeItemsNo(withdrawDetailEntity.getPayTradeItemsNo());
        fundsWithdrawJobEntity.setPayTradeNo(fundsTradeItemsEntity.getPayTradeNo());
        fundsWithdrawJobEntity.setJobType(WithdrawTaskEnum.INVOKE_ACCOUNT_TRANS_OUT.getCode());
        fundsWithdrawJobEntity.setJobTypeDesc(WithdrawTaskEnum.INVOKE_ACCOUNT_TRANS_OUT.getDesc());
        fundsWithdrawJobEntity.setJobStatus(FundsJobStatus.TODO.getCode());
        fundsWithdrawJobEntity.setJobRunCount(0);
        fundsWithdrawJobEntity.setJobLevel("T1");//默认T+1提交
        fundsWithdrawJobEntity.setEnv(env);
        fundsWithdrawJobEntity.setCreateTime(new Date());
        fundsWithdrawJobEntity.setUpdateTime(new Date());
        return fundsWithdrawJobEntity;
    }


    /**
     * 组装提现详情单数据
     *
     * @param fundsTradeItemsEntity
     * @param withdrawApplyRequest
     * @param bankCardInfo
     * @return FundsWithdrawDetailEntity
     */
    private FundsWithdrawDetailEntity composeWithdrawDetail(FundsTradeInfoEntity fundsTradeInfoEntity, FundsTradeItemsEntity fundsTradeItemsEntity, WithdrawApplyRequest withdrawApplyRequest, BankCardInfoResult bankCardInfo) {
        FundsWithdrawDetailEntity fundsWithdrawDetailEntity = new FundsWithdrawDetailEntity();
        fundsWithdrawDetailEntity.setWithdrawDetailNo(serialidGenerator.get());
        fundsWithdrawDetailEntity.setBizTradeNo(fundsTradeInfoEntity.getBizTradeNo());
        fundsWithdrawDetailEntity.setPayTradeNo(fundsTradeItemsEntity.getPayTradeNo());
        fundsWithdrawDetailEntity.setPayTradeItemsNo(fundsTradeItemsEntity.getPayTradeItemsNo());
        fundsWithdrawDetailEntity.setCurrency(Integer.valueOf(withdrawApplyRequest.getCurrencyCode()));
        fundsWithdrawDetailEntity.setWithdrawAmount(fundsTradeItemsEntity.getTradeAmount());
        fundsWithdrawDetailEntity.setWithdrawStatus(WithdrawStatusEnum.APPLYING.getStatus());
        fundsWithdrawDetailEntity.setMerchantNo(withdrawApplyRequest.getMerchantNo());
        fundsWithdrawDetailEntity.setPartnerId(fundsTradeInfoEntity.getPartnerId());
        fundsWithdrawDetailEntity.setCustAccountType(withdrawApplyRequest.getBizProd());
        fundsWithdrawDetailEntity.setInstId(bankCardInfo.getBankCode());
        fundsWithdrawDetailEntity.setInstName(bankCardInfo.getBankName());
        fundsWithdrawDetailEntity.setInstBranchName(bankCardInfo.getSubbranch());
        fundsWithdrawDetailEntity.setInstProvince(bankCardInfo.getProvince());
        fundsWithdrawDetailEntity.setInstCity(bankCardInfo.getCity());
        fundsWithdrawDetailEntity.setInstAccountNo(bankCardInfo.getCardNo());
        fundsWithdrawDetailEntity.setInstAccountName(bankCardInfo.getCardholder());
        fundsWithdrawDetailEntity.setInstAccountType(bankCardInfo.getCardAccountType());
        fundsWithdrawDetailEntity.setInstCardType(bankCardInfo.getCardType());
        fundsWithdrawDetailEntity.setWithdrawNote(buildWithdrawNote(withdrawApplyRequest));
        fundsWithdrawDetailEntity.setCreateTime(new Date());
        fundsWithdrawDetailEntity.setUpdateTime(new Date());
        return fundsWithdrawDetailEntity;
    }

    /**
     * 封装四码一号
     *
     * @param withdrawApplyRequest
     * @return
     */
    private String buildWithdrawNote(WithdrawApplyRequest withdrawApplyRequest) {
        UnifiedBizCode unifiedBizCode = new UnifiedBizCode();
        unifiedBizCode.setBizProdCode(String.valueOf(withdrawApplyRequest.getBizProd()));//产品码
        unifiedBizCode.setBizModeCode(String.valueOf(BizModeEnum.TRADING_MODE_INSTANT.getCode()));//业务模式码
        unifiedBizCode.setBizActionCode(String.valueOf(BizActionEnum.WITHDRAW.getCode()));//业务流向码
        unifiedBizCode.setBizPayToolCode(String.valueOf(PayTool.CITIC_WITHDRAW.getBizSubAction()));//业务流向子码
        unifiedBizCode.setBizChannelCode(String.valueOf(PayTool.CITIC_WITHDRAW.getBizSubAction()));//渠道号 //TODO
        return unifiedBizCode.getFullCode();
    }

    /**
     * 组装业务子单数据
     *
     * @param fundsTradeInfoEntity
     * @return FundsTradeItemsEntity
     */
    private FundsTradeItemsEntity composeTradeItemInfo(FundsTradeInfoEntity fundsTradeInfoEntity) {
        FundsTradeItemsEntity fundsTradeItemsEntity = new FundsTradeItemsEntity();
        fundsTradeItemsEntity.setPayTradeItemsNo(serialidGenerator.get());
        fundsTradeItemsEntity.setPayTradeNo(fundsTradeInfoEntity.getPayTradeNo());
        fundsTradeItemsEntity.setBizTradeNo(fundsTradeInfoEntity.getBizTradeNo());
        fundsTradeItemsEntity.setBizAction(BizActionEnum.WITHDRAW.getCode());
        fundsTradeItemsEntity.setFundsInOut(InOutEnum.OUT.getValue());
        fundsTradeItemsEntity.setTradeAmount(fundsTradeInfoEntity.getTradeAmount());
        fundsTradeItemsEntity.setMerchantNo(fundsTradeInfoEntity.getMerchantNo());
        fundsTradeItemsEntity.setTradeState(fundsTradeInfoEntity.getTradeState());
        fundsTradeItemsEntity.setCreateTime(new Date());
        fundsTradeItemsEntity.setUpdateTime(new Date());
        return fundsTradeItemsEntity;
    }

    /**
     * 组装业务单数据
     *
     * @param withdrawApplyRequest
     * @return FundsTradeItemsEntity
     */
    private FundsTradeInfoEntity composeTradeInfo(WithdrawApplyRequest withdrawApplyRequest) {
        FundsTradeInfoEntity fundsTradeInfoEntity = new FundsTradeInfoEntity();
        fundsTradeInfoEntity.setPayTradeNo(serialidGenerator.get());
        fundsTradeInfoEntity.setBizTradeNo(withdrawApplyRequest.getOutBizNo());
        fundsTradeInfoEntity.setBizTradeName(withdrawApplyRequest.getMemo());
        fundsTradeInfoEntity.setBizProd(withdrawApplyRequest.getBizProd());
        fundsTradeInfoEntity.setBizMode(BizModeEnum.TRADING_MODE_INSTANT.getCode());
        fundsTradeInfoEntity.setBizAction(BizActionEnum.WITHDRAW.getCode());
        fundsTradeInfoEntity.setMerchantNo(Long.valueOf(withdrawApplyRequest.getMerchantNo()));
        fundsTradeInfoEntity.setPartnerId(withdrawApplyRequest.getPartnerId());
        fundsTradeInfoEntity.setCurrency(Integer.valueOf(withdrawApplyRequest.getCurrencyCode()));
        fundsTradeInfoEntity.setTradeAmount(withdrawApplyRequest.getWithdrawAmount());
        fundsTradeInfoEntity.setTradeState(FundsTradeStatus.CREATE.getCode());
        fundsTradeInfoEntity.setSettledState(FundsTradeSettleStatus.NONE.getCode());
        fundsTradeInfoEntity.setNotifyState(FundsTradeNotifyStatus.NONE.getCode());
        fundsTradeInfoEntity.setExtraInfo(withdrawApplyRequest.getExtInfo());
        fundsTradeInfoEntity.setOutBizContext(withdrawApplyRequest.getTraceContext().getTraceId());
        fundsTradeInfoEntity.setCreateTime(new Date());
        fundsTradeInfoEntity.setUpdateTime(new Date());
        return fundsTradeInfoEntity;
    }

    /**
     * 构造返回结果
     *
     * @param fundsTradeInfoEntity
     * @param isIdempotent
     * @return DataResult<WithdrawApplyResult>
     */
    private WithdrawApplyResult buildWithdrawApplyResult(FundsTradeInfoEntity fundsTradeInfoEntity, boolean isIdempotent) {
        WithdrawApplyResult withdrawApplyResult = new WithdrawApplyResult();
        withdrawApplyResult.setIdempotent(isIdempotent);
        withdrawApplyResult.setBizTradeNo(fundsTradeInfoEntity.getBizTradeNo());
        withdrawApplyResult.setPayTradeNo(fundsTradeInfoEntity.getPayTradeNo());
        withdrawApplyResult.setWithdrawStatus(FundsTradeStatus.buildWithdrawStatus(FundsTradeStatus.getByCode(fundsTradeInfoEntity.getTradeState())));
        return withdrawApplyResult;
    }

    /**
     * 初始化上下文
     *
     * @param operatorEnum 操作类型
     */
    protected void initContext(OperatorEnum operatorEnum) {
        FundsWithdrawContext fundsWithdrawContext = new FundsWithdrawContext();
        fundsWithdrawContext.setOperatorEnum(operatorEnum);
        FundsBaseContextHolder.set(fundsWithdrawContext);
    }
}
