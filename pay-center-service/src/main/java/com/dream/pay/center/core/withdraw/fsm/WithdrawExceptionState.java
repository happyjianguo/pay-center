package com.dream.pay.center.core.withdraw.fsm;

import com.dream.center.out.mock.dto.OperationStatusEnum;
import com.dream.pay.center.core.withdraw.enums.WithdrawAutoRepairTypeEnum;
import com.dream.pay.center.core.withdraw.enums.WithdrawTaskEnum;
import com.dream.pay.center.dao.FundsTradeInfoDao;
import com.dream.pay.center.dao.FundsWithdrawDetailDao;
import com.dream.pay.center.dao.FundsWithdrawJobDao;
import com.dream.pay.center.model.FundsWithdrawDetailEntity;
import com.dream.pay.center.model.FundsWithdrawJobEntity;
import com.dream.pay.center.status.FundsTradeStatus;
import com.dream.pay.center.status.FundsWithdrawStatus;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.transaction.support.TransactionTemplate;

import javax.annotation.Resource;
import java.util.Date;
import java.util.Map;

/**
 * 提现异常==自动更新状态驱动提现申请打回／提现申请重试／提现查询重试
 *
 * @author 孟振滨
 * @since 2017-10-23
 */
@Slf4j
public class WithdrawExceptionState implements WithdrawStatusFlow {

    private static Map<String, WithdrawAutoRepairTypeEnum> settleResultRepairType = new HashedMap();

    static {
        settleResultRepairType.put("打回", WithdrawAutoRepairTypeEnum.REJECT_WITHDRAW);
        settleResultRepairType.put("重试", WithdrawAutoRepairTypeEnum.REDO_WITHDRAW_APPLY);
        settleResultRepairType.put("同步", WithdrawAutoRepairTypeEnum.REDO_WITHDRAW_QUERY);
    }

    @Resource
    private TransactionTemplate transactionTemplate;

    @Resource
    protected FundsTradeInfoDao fundsTradeInfoDao;

    @Resource
    protected FundsWithdrawDetailDao fundsWithdrawDetailDao;

    @Resource
    protected FundsWithdrawJobDao fundsWithdrawJobDao;

    @Override
    public OperationStatusEnum statusFlow(FundsWithdrawDetailEntity fundsWithdrawDetailEntity,
                                          FundsWithdrawJobEntity fundsWithdrawJobEntity) {
        if (fundsWithdrawJobEntity.getJobType() != WithdrawTaskEnum.EXCEPTION_SUSPENDED.getCode()) {
            log.info("提现异常处理,状态位不符合执行条件,[{}]!=[{}]", fundsWithdrawJobEntity.getJobType(), WithdrawTaskEnum.EXCEPTION_SUSPENDED.getCode());
            return OperationStatusEnum.UNKNOW;
        }

        log.info("提现进入异常单自动处理流程,本次处理{}", fundsWithdrawDetailEntity.getPayTradeNo());
        return transactionTemplate.execute(status -> {
            String outErrorMsg = fundsWithdrawDetailEntity.getOutErrorMsg();
            if (null == outErrorMsg || "".equals(outErrorMsg)) {
                log.error("{}提现单由于[{}]失败原因未空", fundsWithdrawDetailEntity.getPayTradeNo(), outErrorMsg);
                return OperationStatusEnum.FAIL;
            }
            WithdrawAutoRepairTypeEnum withdrawAutoRepairTypeEnum = settleResultRepairType.get(outErrorMsg);
            if (null == withdrawAutoRepairTypeEnum) {
                log.error("{}提现单[{}]失败,未找到相应的处理方法", fundsWithdrawDetailEntity.getPayTradeNo(), outErrorMsg);
                return OperationStatusEnum.FAIL;
            }

            switch (withdrawAutoRepairTypeEnum) {
                case REJECT_WITHDRAW://直接打回提现申请
                    //更新提现单状态【提现失败】
                    fundsWithdrawDetailEntity.setWithdrawStatus(FundsWithdrawStatus.FAIL.getStatus());
                    fundsWithdrawDetailEntity.setUpdateTime(new Date());
                    fundsWithdrawDetailDao.updateByPrimaryKeySelective(fundsWithdrawDetailEntity);
                    //更新任务为失败
                    fundsWithdrawJobDao.updateTypeById(WithdrawTaskEnum.WITHDRAW_FAIL.getCode(), WithdrawTaskEnum.WITHDRAW_FAIL.getDesc(), fundsWithdrawJobEntity.getId());

                    log.info("{}提现单由于[{}]失败原因,调用[{}]直接打回", fundsWithdrawDetailEntity.getPayTradeNo(), outErrorMsg, withdrawAutoRepairTypeEnum.name());
                    break;
                case REDO_WITHDRAW_APPLY://驱动任务重新发起提现申请
                    fundsWithdrawDetailEntity.setWithdrawStatus(FundsWithdrawStatus.APPLY_SUCCESS.getStatus());
                    fundsWithdrawDetailEntity.setUpdateTime(new Date());
                    fundsWithdrawDetailDao.updateByPrimaryKeySelective(fundsWithdrawDetailEntity);
                    //更新任务为调用渠道提现申请
                    fundsWithdrawJobDao.updateTypeById(WithdrawTaskEnum.INVOKE_CHANNEL_WITHDRAW_APPLY.getCode(), WithdrawTaskEnum.INVOKE_CHANNEL_WITHDRAW_APPLY.getDesc(), fundsWithdrawJobEntity.getId());
                    //更新收单状态【交易处理中】
                    fundsTradeInfoDao.updateStatusByPayTradeNo(FundsTradeStatus.PROCESSING.getCode(), fundsWithdrawDetailEntity.getPayTradeNo());

                    log.info("{}提现单由于[{}]失败原因,调用[{}]重新调用渠道发起提现申请", fundsWithdrawDetailEntity.getPayTradeNo(), outErrorMsg, withdrawAutoRepairTypeEnum.name());
                    break;
                case REDO_WITHDRAW_QUERY://驱动任务重新发起提现查询
                    fundsWithdrawDetailEntity.setWithdrawStatus(FundsWithdrawStatus.PROCESSING.getStatus());
                    fundsWithdrawDetailEntity.setUpdateTime(new Date());
                    fundsWithdrawDetailDao.updateByPrimaryKeySelective(fundsWithdrawDetailEntity);
                    //更新任务为调用渠道提现查询
                    fundsWithdrawJobDao.updateTypeById(WithdrawTaskEnum.INVOKE_CHANNEL_WITHDRAW_QUERY.getCode(), WithdrawTaskEnum.INVOKE_CHANNEL_WITHDRAW_APPLY.getDesc(), fundsWithdrawJobEntity.getId());

                    log.info("{}提现单由于[{}]失败原因,调用[{}]重新调用渠道发起提现查询", fundsWithdrawDetailEntity.getPayTradeNo(), outErrorMsg, withdrawAutoRepairTypeEnum.name());
                    break;
                default:
                    log.error("{}提现单由于[{}]失败原因,未找到相应的处理方法", fundsWithdrawDetailEntity.getPayTradeNo(), outErrorMsg);
                    break;
            }
            return OperationStatusEnum.UNKNOW;
        });
    }
}
