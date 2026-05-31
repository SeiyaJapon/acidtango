package com.acidtangoinditex.productsorter.products.domain.product.valueObjects;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class StockUnitsTest {

    @Test
    void rejects_negative_values() {
        assertThatThrownBy(() -> StockUnits.of(-1))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("stock units cannot be negative");
    }

    @Test
    void zero_is_not_available() {
        assertThat(StockUnits.of(0).isAvailable()).isFalse();
    }

    @Test
    void any_positive_amount_is_available() {
        assertThat(StockUnits.of(1).isAvailable()).isTrue();
        assertThat(StockUnits.of(99).isAvailable()).isTrue();
    }

    @Test
    void preserves_the_value() {
        assertThat(StockUnits.of(42).value()).isEqualTo(42);
    }
}
