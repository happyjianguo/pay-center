package com.dream.pay.center.api.request;

import com.dream.pay.bean.ValidationResult;
import com.dream.pay.center.common.enums.ErrorEnum;
import com.dream.pay.center.common.exception.IllegalArgsException;
import com.dream.pay.utils.ValidationUtils;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 转账申请参数类
 *
 * @author mengzhenbin
 */
@Data
@Setter
@Getter
@ToString(callSuper = true)
public class TransferApplyRequest extends AbstractBaseRequest {
}
