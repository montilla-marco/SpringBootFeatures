package com.montilla.laboratory.myfeatures.common.iod.exception.impl;

import com.montilla.laboratory.myfeatures.common.iod.exception.TierException;

public class DomainException extends TierException {

    public DomainException()
    {
        super();
    }

    public DomainException(String message)
    {
        super(message);
    }

    public DomainException(String message, Throwable cause)
    {
        super(message, cause);
    }
}
