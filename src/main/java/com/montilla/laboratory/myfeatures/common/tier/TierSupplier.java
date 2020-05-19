package com.montilla.laboratory.myfeatures.common.tier;

import java.util.function.Supplier;

@FunctionalInterface
public interface TierSupplier<T> extends Supplier<T> {
}
