package com.montilla.laboratory.myfeatures.common.iod.exception.handler;


import com.montilla.laboratory.myfeatures.common.iod.exception.TierException;
import com.montilla.laboratory.myfeatures.common.iod.exception.TierExceptionHandler;
import com.montilla.laboratory.myfeatures.common.iod.exception.impl.GatewayException;

public class GatewayExceptionHandlerImpl implements TierExceptionHandler<TierException> {

    @Override
    public RuntimeException handleRuntimeException(TierException e) {
        return new GatewayException(e.getMessage());
    }

    @Override
    public RuntimeException handleException(Exception exception) {
        return new GatewayException("LOG MESSAGE");
    }
}
