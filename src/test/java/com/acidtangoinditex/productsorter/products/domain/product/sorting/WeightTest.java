package com.acidtangoinditex.productsorter.products.domain.product.sorting;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class WeightTest {

    @Test
    void rejects_null_value() {
        assertThatThrownBy(() -> Weight.of((BigDecimal) null))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void rejects_negative_value() {
        assertThatThrownBy(() -> Weight.of(-0.01))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void rejects_value_above_one() {
        assertThatThrownBy(() -> Weight.of(1.0001))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void accepts_zero() {
        assertThat(Weight.of(0.0).isZero()).isTrue();
    }

    @Test
    void accepts_value_at_upper_bound() {
        assertThat(Weight.of(1.0).value()).isEqualByComparingTo(BigDecimal.ONE);
    }

    @Test
    void non_zero_value_is_not_zero() {
        assertThat(Weight.of(0.0001).isZero()).isFalse();
    }

    @Test
    void of_big_decimal_overload_preserves_the_value() {
        Weight weight = Weight.of(new BigDecimal("0.5"));
        assertThat(weight.value()).isEqualByComparingTo(new BigDecimal("0.5"));
    }
}
