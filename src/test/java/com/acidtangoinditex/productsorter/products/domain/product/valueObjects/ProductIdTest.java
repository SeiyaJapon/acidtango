package com.acidtangoinditex.productsorter.products.domain.product.valueObjects;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ProductIdTest {

    @Test
    void rejects_zero() {
        assertThatThrownBy(() -> ProductId.of(0))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void rejects_negative_values() {
        assertThatThrownBy(() -> ProductId.of(-1))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void preserves_positive_values() {
        assertThat(ProductId.of(42).value()).isEqualTo(42L);
    }
}
