package com.montilla.laboratory.myfeatures.common.iod.exception.handler;

import com.montilla.laboratory.myfeatures.common.iod.exception.TierExceptionHandler;
import com.montilla.laboratory.myfeatures.common.iod.exception.impl.ApiException;
import com.montilla.laboratory.myfeatures.common.iod.exception.impl.DomainException;

public class ApiExceptionHandlerImpl implements TierExceptionHandler<DomainException> {

    @Override
    public RuntimeException handleRuntimeException(DomainException e) {
        return new ApiException(e.getMessage());
    }

    @Override
    public RuntimeException handleException(Exception exception) {
        return new ApiException("LOGMESSAGE");
    }
}
