package com.montilla.laboratory.myfeatures.common.tier;

import com.montilla.laboratory.myfeatures.common.tier.exception.TierException;
import com.montilla.laboratory.myfeatures.common.tier.exception.TierExceptionHandler;

import java.util.Objects;
import java.util.function.Function;

public interface TierFunction<O, T> extends Function<O, T> {

    T apply(O input);

    static <O, T> TierFunction<O, T> executeCall(TierCall call) {
        Objects.requireNonNull(call, "TierCall can not be null executing TierFunction");
        return input -> {
            Objects.requireNonNull(input, "Input can not be null executing TierFunction");
            return (T) call.call(input);
        };
    }

    interface OneParam< O, T, S> {
        T execute(TierExceptionHandler<S> tierExceptionHandler, O input);

        default TierFunction<O, T> exceptionHandler(TierExceptionHandler exceptionHandler) {
            return input -> (T) execute(exceptionHandler, input);
        }
    }

    static < O, T, S> OneParam< O, T, S> executeTierFunctionWithExceptionHandler(TierCall tierCall) {
        Objects.requireNonNull(tierCall, "Call can not be null in TierFunction");
        return (tierExceptionHandler, input) -> {
            try {
                return (T) tierCall.call(input);
            } catch (TierException e) {
                throw tierExceptionHandler.handleRuntimeException((S) e);
            } catch (Exception e) {
                throw tierExceptionHandler.handleException(e);
            }
        };
    }
}
