package com.montilla.laboratory.myfeatures.common.iod.exception.handler;


import com.montilla.laboratory.myfeatures.common.iod.exception.TierExceptionHandler;
import com.montilla.laboratory.myfeatures.common.iod.exception.impl.DataException;
import com.montilla.laboratory.myfeatures.common.iod.exception.impl.GatewayException;

public class DataExceptionHandlerImpl implements TierExceptionHandler<GatewayException> {

    @Override
    public RuntimeException handleRuntimeException(GatewayException e) {
        return new DataException(e.getMessage());
    }

    @Override
    public RuntimeException handleException(Exception exception) {
        return new DataException("LOG MESSAGE");
    }
}
