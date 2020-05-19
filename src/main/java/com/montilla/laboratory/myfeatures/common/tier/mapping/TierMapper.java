package com.montilla.laboratory.myfeatures.common.tier.mapping;

import com.montilla.laboratory.myfeatures.common.tier.TierCall;
import com.montilla.laboratory.myfeatures.common.tier.TierFunction;

import java.util.Objects;

@FunctionalInterface
public interface TierMapper<I, O> extends TierCall<I, O> {

    default TierMapper<I, O> thenApplyProperty(TierFunction propertySetter) {
        Objects.requireNonNull(propertySetter, "PropertySetter can not be null in TierMapper[thenApplyProperty]");
        return input -> {
            Objects.requireNonNull(input, "input can not be null in TierMapper[thenApplyProperty]");
            O output = this.call(input);
            return (O) propertySetter.apply(output);
        };
    }
}
