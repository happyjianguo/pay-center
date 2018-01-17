package com.dream.center.out.mock.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * 清结算退款冻结操作参数
 *
 * @Author mengzhenbin
 * @Since 2017/12/13
 */
@Setter
@Getter
public class PretreatmentRequest {

    @NotNull(message = "商户号不能为空")
    private String merchantNo;

    private String remark;

    @Size(min = 1, message = "预处理退款明细数量度必须大于等于1")
    private List<PrestreatmentInfo> prestreatmentInfoList;
}
