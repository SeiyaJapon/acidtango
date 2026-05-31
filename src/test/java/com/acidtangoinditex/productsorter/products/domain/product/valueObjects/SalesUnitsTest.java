package com.acidtangoinditex.productsorter.products.domain.product.valueObjects;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class SalesUnitsTest {

    @Test
    void rejects_negative_values() {
        assertThatThrownBy(() -> SalesUnits.of(-1))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void allows_zero() {
        assertThat(SalesUnits.of(0).value()).isZero();
    }

    @Test
    void preserves_the_value() {
        assertThat(SalesUnits.of(650).value()).isEqualTo(650);
    }
}
