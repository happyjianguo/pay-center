package com.dream.pay.center.service.context;

import com.dream.pay.center.common.enums.OperatorEnum;
import com.dream.pay.center.model.FundsTradeInfoEntity;
import com.dream.pay.center.model.FundsTradeItemsEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 收单统一上下文
 *
 * Created by mengzhenbin on 16/7/21.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FundsBaseContext {
  /**
   * 操作类型
   */
  private OperatorEnum operatorEnum;

  /**
   * 业务订单数据
   */
  private FundsTradeItemsEntity fundsTradeItemsEntity;
  /**
   * 业务订单子单数据
   */
  private FundsTradeInfoEntity fundsTradeInfoEntity;
}
