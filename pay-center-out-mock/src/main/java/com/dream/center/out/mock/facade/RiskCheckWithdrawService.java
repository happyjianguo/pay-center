package com.dream.center.out.mock.facade;

import com.dream.pay.bean.DataResult;

/**
 * Created by socket on 2017/11/17.
 */
public class RiskCheckWithdrawService {

    public static DataResult<Integer> withdrawBlackCheck(String merchantNo) {
        DataResult<Integer> result = new DataResult<Integer>();
        result.setSuccess(true);
        result.setCode("200");
        result.setMessage("SUCCESS");
        result.setData(0);
        return result;
    }
}
