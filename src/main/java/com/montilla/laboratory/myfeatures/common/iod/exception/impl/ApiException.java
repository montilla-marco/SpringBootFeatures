package com.montilla.laboratory.myfeatures.common.iod.exception.impl;

import com.montilla.laboratory.myfeatures.common.iod.exception.TierException;

public class ApiException extends TierException {

    public ApiException()
    {
        super();
    }

    public ApiException(String message)
    {
        super(message);
    }

    public ApiException(String message, Throwable cause)
    {
        super(message, cause);
    }
}
