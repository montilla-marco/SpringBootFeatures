package com.montilla.laboratory.myfeatures.common.iod.exception.impl;

import com.montilla.laboratory.myfeatures.common.iod.exception.TierException;

public class DataException extends TierException {

    public DataException()
    {
        super();
    }

    public DataException(String message)
    {
        super(message);
    }

    public DataException(String message, Throwable cause)
    {
        super(message, cause);
    }
}
