package com.montilla.laboratory.myfeatures.common.iod;

import com.montilla.laboratory.myfeatures.common.iod.exception.TierException;
import com.montilla.laboratory.myfeatures.common.iod.exception.TierExceptionHandler;

import java.util.Objects;

public abstract class AbstractionTemplate<I, O> {

    private RepositoryCall repositoryCall;

    private TierExceptionHandler exceptionHandler;

    public AbstractionTemplate(RepositoryCall repositoryCall, TierExceptionHandler exceptionHandler) {
        Objects.requireNonNull(repositoryCall, "RepositoryCall can not be null building a AbstractionTemplate");
        Objects.requireNonNull(exceptionHandler, "TierExceptionHandler can not be null building a AbstractionTemplate");
        this.repositoryCall = repositoryCall;
        this.exceptionHandler = exceptionHandler;
    }

    public O repoConnect(I input) {
        Objects.requireNonNull(input, "input can not be null in repoConnect");
        return  AbstractionCall.<I, O, TierException>
                executeTierFunctionWithExceptionHandler(repositoryCall)
                .exceptionHandler(exceptionHandler)
                .apply(input);
    }
}
