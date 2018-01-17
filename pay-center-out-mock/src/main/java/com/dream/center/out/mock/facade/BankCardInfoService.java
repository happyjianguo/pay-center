package com.dream.center.out.mock.facade;

import com.dream.center.out.mock.dto.BankCardInfoResult;
import com.dream.center.out.mock.dto.GetBankCardInfoRequest;
import com.dream.pay.bean.DataResult;

/**
 * Created by socket on 2017/11/17.
 */
public class BankCardInfoService {

    public static DataResult<BankCardInfoResult> queryCardInfo(GetBankCardInfoRequest getBankCardInfoRequest) {
        DataResult<BankCardInfoResult> result = new DataResult<BankCardInfoResult>();

        result.setSuccess(true);
        result.setCode("200");
        result.setMessage("SUCCESS");
        BankCardInfoResult bankCardInfo = new BankCardInfoResult();
        bankCardInfo.setBankCode("CMB");
        bankCardInfo.setBankName("招商银行");
        bankCardInfo.setBankUnitedCode("600600");
        bankCardInfo.setBindType("3");
        bankCardInfo.setCardAccountType("0");
        bankCardInfo.setCardBindId(888L);
        bankCardInfo.setCardholder("孟振滨");
        bankCardInfo.setCardNo("888888888888");
        bankCardInfo.setCardType("1");
        bankCardInfo.setCity("BJ");
        bankCardInfo.setProvince("BJ");
        bankCardInfo.setMobile("18611073586");
        bankCardInfo.setSubbranch("静安庄支行");
        result.setData(bankCardInfo);
        return result;
    }
}
