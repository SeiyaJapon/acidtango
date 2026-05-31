package com.acidtangoinditex.productsorter.products.domain.product.sorting;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.Objects;

public record Score(BigDecimal value) implements Comparable<Score> {

    public static final Score ZERO = new Score(BigDecimal.ZERO);

    public Score {
        Objects.requireNonNull(value, "score value cannot be null");
        if (value.signum() < 0) {
            throw new IllegalArgumentException("score cannot be negative: " + value);
        }
    }

    public static Score of(BigDecimal value) {
        return new Score(value);
    }

    public static Score of(double value) {
        return new Score(BigDecimal.valueOf(value));
    }

    public Score plus(Score other) {
        return new Score(this.value.add(other.value));
    }

    public Score weightedBy(Weight weight) {
        return new Score(this.value.multiply(weight.value(), MathContext.DECIMAL64));
    }

    @Override
    public int compareTo(Score other) {
        return this.value.compareTo(other.value);
    }
}
