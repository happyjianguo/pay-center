package com.dream.pay.center.api.request;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 充值申请参数类
 *
 * @author mengzhenbin
 */
@Data
@Setter
@Getter
@ToString(callSuper = true)
public class RechargeApplyRequest extends UnifiedOrderCreateRequest {

}
