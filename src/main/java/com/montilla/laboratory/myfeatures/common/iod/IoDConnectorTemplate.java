package com.montilla.laboratory.myfeatures.common.iod;

import com.montilla.laboratory.myfeatures.common.iod.exception.TierExceptionHandler;
import com.montilla.laboratory.myfeatures.common.iod.mapping.TierMapper;

import java.util.Objects;

public abstract class IoDConnectorTemplate<I, O> {

    private TierMapper requestMapper;

    private AbstractionCall abstractionCall;

    private TierMapper responseMapper;

    private TierExceptionHandler exceptionHandler;


    public IoDConnectorTemplate(TierMapper requestMapper,
                                AbstractionCall abstractionCall,
                                TierMapper responseMapper,
                                TierExceptionHandler exceptionHandler) {
        Objects.requireNonNull(requestMapper, "UpTierRequestTierMapper can not be null building a IoDConnectorTemplate");
        Objects.requireNonNull(abstractionCall, "AbstractionCall can not be null building a IoDConnectorTemplate");
        Objects.requireNonNull(responseMapper, "UpTierResponseTierMapper can not be null building a IoDConnectorTemplate");
        Objects.requireNonNull(exceptionHandler, "TierExceptionHandler can not be null building a IoDConnectorTemplate");
        this.requestMapper = requestMapper;
        this.abstractionCall = abstractionCall;
        this.responseMapper = responseMapper;
        this.exceptionHandler = exceptionHandler;
    }

    public IoDConnectorTemplate() {

    }


    public O execute(I input) {
        return (O) IoDConnector
                        .buildWithExceptionHandler(exceptionHandler)
                        .applyUpTierRequestMapper(requestMapper)
                        .executeCall(abstractionCall)
                        .applyUpTierResponseMapper(responseMapper)
                        .connect(input);

    }
}
