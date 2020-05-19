package com.montilla.laboratory.myfeatures.common.tier;

import com.montilla.laboratory.myfeatures.common.tier.exception.TierExceptionHandler;
import com.montilla.laboratory.myfeatures.common.tier.mapping.TierMapper;

public abstract class TierTemplate<I, O> {

    private TierMapper requestMapper;

    private TierFunction tierFunction;

    private TierMapper responseMapper;

    private TierExceptionHandler genericExceptionHandler;


    public TierTemplate(TierMapper requestMapper,
                        TierFunction tierFunction,
                        TierMapper responseMapper,
                        TierExceptionHandler genericExceptionHandler) {
        this.requestMapper = requestMapper;
        this.tierFunction = tierFunction;
        this.responseMapper = responseMapper;
        this.genericExceptionHandler = genericExceptionHandler;
    }


/*    public O executeLogic(I input) {



    }*/
    /*public abstract T execute();

    public abstract T execute(String ...headers);*/

    public O execute(I input) {
        return (O) TierCommand.executeWithExceptionHandler(input)
                .exceptionHandler(genericExceptionHandler)
                .applyRequestMapper(requestMapper)
                .executeCall(tierFunction)
                .applyResponseMapper(responseMapper)
                .execute();
    }
}
