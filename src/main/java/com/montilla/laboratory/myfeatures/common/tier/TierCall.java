package com.montilla.laboratory.myfeatures.common.tier;

@FunctionalInterface
public interface TierCall<I, O> {

    O call(I input);

}
