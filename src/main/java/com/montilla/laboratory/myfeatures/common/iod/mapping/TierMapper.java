package com.montilla.laboratory.myfeatures.common.iod.mapping;

import com.montilla.laboratory.myfeatures.common.iod.AbstractionCall;

import java.util.Objects;

@FunctionalInterface
public interface TierMapper<I, O> {

    O map(I input);

    default TierMapper<I, O> thenApplyProperty(MapperHelpler propertySetter) {
        Objects.requireNonNull(propertySetter, "PropertySetter can not be null in TierMapper[thenApplyProperty]");
        return input -> {
            Objects.requireNonNull(input, "input can not be null in TierMapper[thenApplyProperty]");
            O output = this.map(input);
            propertySetter.applyPropertie(output);
            return output;
        };
    }
}
