package com.montilla.laboratory.myfeatures.common.iod.exception.impl;

import com.montilla.laboratory.myfeatures.common.iod.exception.TierException;

public class GatewayException extends TierException {

    private static final long serialVersionUID = 1L;

    public GatewayException()
    {
        super();
    }

    public GatewayException(String message)
    {
        super(message);
    }

    public GatewayException(String message, Throwable cause)
    {
        super(message, cause);
    }
}
