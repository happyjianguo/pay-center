package com.dream.pay.center.api.impl;

import com.dream.pay.bean.DataResult;
import com.dream.pay.center.api.facade.UnifiedTransferService;
import com.dream.pay.center.api.request.TransferApplyRequest;
import com.dream.pay.center.api.response.TransferApplyResult;
import lombok.extern.slf4j.Slf4j;

import javax.ws.rs.POST;
import javax.ws.rs.Path;

/**
 * Created by mengzhenbin on 2017/10/13.
 */
@Path("/transfer")
@Slf4j
public class UnifiedTransferServiceImpl implements UnifiedTransferService {
    @POST
    @Path("/apply")
    public DataResult<TransferApplyResult> transferApply(TransferApplyRequest transferApplyRequest) {
        return null;
    }
}
