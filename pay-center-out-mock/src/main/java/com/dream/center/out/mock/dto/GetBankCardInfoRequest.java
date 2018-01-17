package com.dream.center.out.mock.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 获取卡信息请求类
 *
 * @Author mengzhenbin
 * @Since 2017/12/2
 */
@Setter
@Getter
public class GetBankCardInfoRequest implements Serializable {
    private static final long serialVersionUID = -9090041653307819958L;

    @NotNull(message = "商户号不能为空")
    private String userNo;
    @NotNull(message = "绑卡ID不能为空")
    private Long cardBindId;

    private Integer actionTypeCode;
}
