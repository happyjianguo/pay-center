package com.dream.pay.center.common.constants;

import com.dream.pay.center.common.validators.BizCodeValidate;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Author mengzhenbin
 * @Since 2018/1/10
 */
public class UnifiedBizCode implements Serializable {

    private static final int BIZ_PROD_CODE_IDX = 0;
    private static final int BIZ_MODE_CODE_IDX = 1;
    private static final int BIZ_ACTION_CODE_IDX = 2;
    private static final int BIZ_PAY_TOOL_CODE_IDX = 3;
    private static final int BIZ_CHANNEL_CODE_IDX = 4;
    private final String DEF_CODE = "0000";
    private String[] codeStore = new String[]{DEF_CODE, DEF_CODE, DEF_CODE, DEF_CODE, DEF_CODE};

    public String[] getCodeOfArray() {
        return (String[]) this.codeStore.clone();
    }

    public void setBizProdCode(String code) {
        BizCodeValidate.checkSingleCode(code);
        this.codeStore[BIZ_PROD_CODE_IDX] = code;
    }

    public String getBizProdCode() {
        return this.codeStore[BIZ_PROD_CODE_IDX];
    }

    public void setBizModeCode(String code) {
        BizCodeValidate.checkSingleCode(code);
        this.codeStore[BIZ_MODE_CODE_IDX] = code;
    }

    public String getBizModeCode() {
        return this.codeStore[BIZ_MODE_CODE_IDX];
    }

    public void setBizActionCode(String code) {
        BizCodeValidate.checkSingleCode(code);
        this.codeStore[BIZ_ACTION_CODE_IDX] = code;
    }

    public String getBizActionCode() {
        return this.codeStore[BIZ_ACTION_CODE_IDX];
    }

    public void setBizPayToolCode(String code) {
        BizCodeValidate.checkSingleCode(code);
        this.codeStore[BIZ_PAY_TOOL_CODE_IDX] = code;
    }

    public String getBizPayToolCode() {
        return this.codeStore[BIZ_PAY_TOOL_CODE_IDX];
    }

    public void setBizChannelCode(String code) {
        BizCodeValidate.checkSingleCode(code);
        this.codeStore[BIZ_CHANNEL_CODE_IDX] = code;
    }

    public String getBizChannelCode() {
        return this.codeStore[BIZ_CHANNEL_CODE_IDX];
    }

    public String getFullCode() {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < 5; ++i) {
            sb.append(this.codeStore[i]);
        }

        return sb.toString();
    }
}
