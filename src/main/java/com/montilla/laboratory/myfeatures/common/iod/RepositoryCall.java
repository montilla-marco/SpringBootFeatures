package com.montilla.laboratory.myfeatures.common.iod;

@FunctionalInterface
public interface RepositoryCall<I, O> {

    O call(I input);

}
