package com.montilla.laboratory.myfeatures.common.iod;

import com.montilla.laboratory.myfeatures.common.iod.exception.TierException;
import com.montilla.laboratory.myfeatures.common.iod.exception.TierExceptionHandler;
import com.montilla.laboratory.myfeatures.common.iod.mapping.TierMapper;

import java.util.Objects;

@FunctionalInterface
public interface IoDConnector<T, D> {

    D connect(T upTierRequest);

    interface UpTierResponseMapper<O, D, T> {
        D connect(T upTierRequest, TierMapper<O , D> upTierResponseMapper);

        default IoDConnector<T, D> applyUpTierResponseMapper(TierMapper upTierResponseMapper) {
            Objects.requireNonNull(upTierResponseMapper, "UpTierResponseMapper can not be null in IoDConnector");
            return upTierRequest -> (D) connect(upTierRequest, upTierResponseMapper);
        }
    }

    interface CallExecutor<I, O, T, D> {
        O connect(T upTierRequest,
                  AbstractionCall<I, O> abstractionCall,
                  TierMapper<O , D> upTierResponseMapper);

        default UpTierResponseMapper<O, D, T> executeCall(AbstractionCall abstractionCall) {
            Objects.requireNonNull(abstractionCall, "AbstractionCall can not be null in IoDConnector");
            return (upTierRequest, upTierResponseMapper) -> (D) connect(upTierRequest, abstractionCall, upTierResponseMapper);
        }

    }

    interface UpTierRequestMapper<I, O, T, D> {
        D connect(T upTierRequest,
                  TierMapper<T, I> upTierRequestMapper,
                  AbstractionCall<I, O> abstractionCall,
                  TierMapper<O , D> upTierResponseMapper);

        default CallExecutor<I, O, T, D> applyUpTierRequestMapper(TierMapper upTierRequestMapper) {
            Objects.requireNonNull(upTierRequestMapper, "UpTierRequestMapper can not be null in IoDConnector");
            return (upTierRequest, tierFunction, upTierResponseMapper) -> (O) connect(upTierRequest, upTierRequestMapper, tierFunction, upTierResponseMapper);
        }

    }

    static <I, O, T, D, S extends RuntimeException> UpTierRequestMapper<I, O, T, D> buildWithExceptionHandler(TierExceptionHandler<S> exceptionHandler) {
        Objects.requireNonNull(exceptionHandler, "TierExceptionHandler can not be null in IoDConnector");
        return (upTierRequest, upTierRequestMapper, tierFunction, upTierResponseMapper) -> {
            Objects.requireNonNull(upTierRequest, "upTierRequest can not be null in IoDConnector");
            try {
                 I lowTierRequest = upTierRequestMapper.map(upTierRequest);
                 Objects.requireNonNull(lowTierRequest, "lowTierRequest can not be null while try map upTierRequest to lowTierRequest");
                 O lowTierResponse = tierFunction.apply(lowTierRequest);
                 Objects.requireNonNull(lowTierResponse, "lowTierResponse can not be null while try make a AbstractionCall");
                 D upTierResponse = upTierResponseMapper.map(lowTierResponse);
                 Objects.requireNonNull(upTierResponse, "lowTierResponse can not be null while try make a AbstractionCall");
                 return upTierResponse;
            } catch (TierException e) {
                throw exceptionHandler.handleRuntimeException((S) e);
            } catch (Exception e) {
                throw exceptionHandler.handleException(e);
            }
        };
    }


}
