package com.acidtangoinditex.productsorter.products.domain.product.valueObjects;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ProductNameTest {

    @Test
    void rejects_null_value() {
        assertThatThrownBy(() -> ProductName.of(null))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void rejects_blank_value() {
        assertThatThrownBy(() -> ProductName.of("   "))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void rejects_overly_long_value() {
        String tooLong = "x".repeat(201);
        assertThatThrownBy(() -> ProductName.of(tooLong))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void accepts_value_at_max_length() {
        String atLimit = "x".repeat(200);
        assertThat(ProductName.of(atLimit).value()).isEqualTo(atLimit);
    }

    @Test
    void preserves_the_value() {
        assertThat(ProductName.of("V-NECH BASIC SHIRT").value()).isEqualTo("V-NECH BASIC SHIRT");
    }
}
