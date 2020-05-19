package com.montilla.laboratory.myfeatures.common.tier.exception;

public class TierException  extends RuntimeException  {

    private static final long serialVersionUID = 1L;

    public TierException()
    {
        super();
    }

    public TierException(String message)
    {
        super(message);
    }

    public TierException(String message, Throwable cause)
    {
        super(message, cause);
    }
}
