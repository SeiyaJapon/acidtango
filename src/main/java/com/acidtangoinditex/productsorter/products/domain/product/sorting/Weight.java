package com.acidtangoinditex.productsorter.products.domain.product.sorting;

import java.math.BigDecimal;
import java.util.Objects;

public record Weight(BigDecimal value) {

    public static final Weight ZERO = new Weight(BigDecimal.ZERO);

    public Weight {
        Objects.requireNonNull(value, "weight value cannot be null");
        if (value.signum() < 0) {
            throw new IllegalArgumentException("weight cannot be negative: " + value);
        }
        if (value.compareTo(BigDecimal.ONE) > 0) {
            throw new IllegalArgumentException("weight cannot exceed 1: " + value);
        }
    }

    public static Weight of(BigDecimal value) {
        return new Weight(value);
    }

    public static Weight of(double value) {
        return new Weight(BigDecimal.valueOf(value));
    }

    public boolean isZero() {
        return value.signum() == 0;
    }
}
