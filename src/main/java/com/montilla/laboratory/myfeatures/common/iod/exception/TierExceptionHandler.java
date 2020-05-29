package com.montilla.laboratory.myfeatures.common.iod.exception;

public interface TierExceptionHandler <S>  {

    default RuntimeException handleRuntimeException(S exception){
        throw new TierException("ERROR: GenericExceptionHandler.handleRuntimeException not implement");
    }

    default RuntimeException handleException(Exception e) {
        throw new TierException("ERROR: GenericExceptionHandler.handleException not implement");
    }

}
