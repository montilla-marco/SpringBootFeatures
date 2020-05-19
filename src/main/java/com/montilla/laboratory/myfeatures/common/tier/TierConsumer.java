package com.montilla.laboratory.myfeatures.common.tier;

import java.util.Objects;

@FunctionalInterface
public interface TierConsumer<T> {

    void accept(T t);

    static <T> TierConsumer< T> executeCall(TierCall tierCall) {
        Objects.requireNonNull(tierCall, "TierCall can not be null executing TierConsumer");
        return input -> {
            Objects.requireNonNull(input, "Input can not be null executing TierConsumer");
            tierCall.call(input);
        };
    }

}
