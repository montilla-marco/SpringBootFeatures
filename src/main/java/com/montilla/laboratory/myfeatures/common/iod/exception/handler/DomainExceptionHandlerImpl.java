package com.montilla.laboratory.myfeatures.common.iod.exception.handler;

import com.montilla.laboratory.myfeatures.common.iod.exception.TierExceptionHandler;
import com.montilla.laboratory.myfeatures.common.iod.exception.impl.DataException;
import com.montilla.laboratory.myfeatures.common.iod.exception.impl.DomainException;

public class DomainExceptionHandlerImpl implements TierExceptionHandler<DataException> {

    @Override
    public RuntimeException handleRuntimeException(DataException e) {
        return new DomainException(e.getMessage());
    }

    @Override
    public RuntimeException handleException(Exception exception) {
        return new DomainException("LOG MESSAGE");
    }
}
