package com.dream.center.out.mock.dto;


import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 获取卡信息结果类
 *
 * @Author mengzhenbin
 * @Since 2017/12/2
 */
@Setter
@Getter
public class BankCardInfoResult implements Serializable {
    /**
     * 用户编号
     */
    private long userNo;
    /**
     * 账户类型：对公，对私
     */
    private String cardAccountType;

    /**
     * 绑卡类型：支付，充值，提现
     */
    private String bindType;
    /**
     * 绑卡ID
     */
    private Long cardBindId;
    /**
     * 卡号
     */
    private String cardNo;
    /**
     * 卡类型：借记卡，贷记卡
     */
    private String cardType;
    /**
     * 持卡人姓名
     */
    private String cardholder;
    /**
     * 预留手机号
     */
    private String mobile;
    /**
     * 银行编码
     */
    private String bankCode;
    /**
     * 银行名称
     */
    private String bankName;
    /**
     * 银行联行号
     */
    private String bankUnitedCode;
    /**
     * 开户省
     */
    private String province;
    /**
     * 开户市
     */
    private String city;
    /**
     * 开户支行
     */
    private String subbranch;
}
