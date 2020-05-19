package com.montilla.laboratory.myfeatures.common.tier;


import com.montilla.laboratory.myfeatures.common.tier.exception.TierException;
import com.montilla.laboratory.myfeatures.common.tier.exception.TierExceptionHandler;
import com.montilla.laboratory.myfeatures.common.tier.mapping.TierMapper;

import java.util.Objects;

@FunctionalInterface
public interface TierCommand<D> {

    D execute();

    interface OneParam<T, D> {
        T execute(TierMapper<T, D> responseTierMapper);

        default TierCommand<D> applyResponseMapper(TierMapper responseTierMapper) {
            Objects.requireNonNull(responseTierMapper, "Response TierMapper can not be null in TierCommand");
            return () -> (D) execute(responseTierMapper);
        }
    }

    interface TwoParams<O, T, D> {
        O execute(TierFunction<O, T> tierFunction, TierMapper<T, D> responseTierMapper);

        default OneParam<T, D> executeCall(TierFunction tierFunction) {
            Objects.requireNonNull(tierFunction, "Call can not be null in TierCommand");
            return responseMapper -> (T) execute(tierFunction, responseMapper);
        }
    }

    interface ThreeParams<I, O, T, D> {
        D execute(TierMapper<I, O> requestTierMapper, TierFunction<O, T> tierFunction, TierMapper<T, D> responseTierMapper);

        default TwoParams<O, T, D> applyRequestMapper(TierMapper requestTierMapper) {
            Objects.requireNonNull(requestTierMapper, "Request TierMapper can not be null in TierCommand");
            return (tierFunction, responseMapper) -> (O) execute(requestTierMapper, tierFunction, responseMapper);
        }

    }

    static <I, O, T, D> ThreeParams<I, O, T, D> executeWithOutExceptionHandler(I input) {
        Objects.requireNonNull(input, "Input can not be null in TierCommand");
        return (requestMapper, tierFunction, responseMapper) -> getApiOutPut(input, requestMapper, tierFunction, responseMapper);
    }

    interface FourParams<I, O, T, D,  S> {

        D execute(TierExceptionHandler<S> exceptionHandler,
                  TierMapper<I, O> requestGenericMapper,
                  TierFunction<O, T> tierFunction,
                  TierMapper<T, D> responseGenericMapper);

        default ThreeParams<I, O, T, D> exceptionHandler(TierExceptionHandler exceptionHandler) {
            return (requestMapper, tierFunction, responseMapper) -> (D) execute(exceptionHandler, requestMapper, tierFunction, responseMapper);
        }
    }

    static <I, O, T, D, S extends RuntimeException> FourParams<I, O, T, D, S> executeWithExceptionHandler(I input) {
        Objects.requireNonNull(input, "Input can not be null in  TierCommand");
        return (exceptionHandler, requestMapper, tierFunction, responseMapper) -> {
            try {
                return getApiOutPut(input, requestMapper, tierFunction, responseMapper);
            } catch (TierException e) {
                throw exceptionHandler.handleRuntimeException((S) e);
            } catch (Exception e) {
                throw exceptionHandler.handleException(e);
            }
        };
    }

    static <I, O, T, D> D getApiOutPut(I input, TierMapper<I, O> requestTierMapper, TierFunction<O, T> tierFunction, TierMapper<T, D> responseTierMapper) {
        O inputTierRequestEntity = requestTierMapper.call(input);
        Objects.requireNonNull(inputTierRequestEntity, "Error while try map input to tier request");
        T outputTierResponseEntity = tierFunction.apply(inputTierRequestEntity);
        Objects.requireNonNull(outputTierResponseEntity, "Error while executetier function");
        D inputTierResponse = responseTierMapper.call(outputTierResponseEntity);
        Objects.requireNonNull(inputTierResponse, "Error while  try map domain response to output");
        return inputTierResponse;
    }

}
