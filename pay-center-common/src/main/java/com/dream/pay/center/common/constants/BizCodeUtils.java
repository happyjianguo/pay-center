package com.dream.pay.center.common.constants;

import com.dream.pay.center.common.validators.BizCodeValidate;

/**
 * @Author mengzhenbin
 * @Since 2018/1/10
 */
public class BizCodeUtils {
    private static final String REG_EXP = "(?<=\\G.{4})";

    public static UnifiedBizCode parse(String code) {
        BizCodeValidate.checkFullCode(code);
        String[] codes = code.split("(?<=\\G.{4})");
        UnifiedBizCode unifiedBizCode = new UnifiedBizCode();
        unifiedBizCode.setBizProdCode(codes[0]);
        unifiedBizCode.setBizModeCode(codes[1]);
        unifiedBizCode.setBizActionCode(codes[2]);
        unifiedBizCode.setBizPayToolCode(codes[3]);
        unifiedBizCode.setBizChannelCode(codes[4]);
        return unifiedBizCode;
    }
}
