package com.montilla.laboratory.myfeatures.common.iod;

import com.montilla.laboratory.myfeatures.common.iod.exception.TierException;
import com.montilla.laboratory.myfeatures.common.iod.exception.TierExceptionHandler;

import java.util.Objects;

public interface AbstractionCall<O, T> {

    T apply(O input);

    interface OneParam< O, T, S> {
        T execute(TierExceptionHandler<S> tierExceptionHandler, O input);

        default AbstractionCall<O, T> exceptionHandler(TierExceptionHandler exceptionHandler) {
            Objects.requireNonNull(exceptionHandler, "TierExceptionHandler can not be null in AbstractionCall");
            return input -> (T) execute(exceptionHandler, input);
        }
    }

    static < O, T, S> OneParam< O, T, S> executeTierFunctionWithExceptionHandler(RepositoryCall repositoryCall) {
        Objects.requireNonNull(repositoryCall, "RepositoryCall can not be null in AbstractionCall");
        return (tierExceptionHandler, input) -> {
            Objects.requireNonNull(input, "Input can not be null in AbstractionCall");
            try {
                return (T) repositoryCall.call(input);
            } catch (TierException e) {
                throw tierExceptionHandler.handleRuntimeException((S) e);
            } catch (Exception e) {
                throw tierExceptionHandler.handleException(e);
            }
        };
    }
}
