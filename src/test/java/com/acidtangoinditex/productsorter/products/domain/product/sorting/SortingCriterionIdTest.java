package com.acidtangoinditex.productsorter.products.domain.product.sorting;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class SortingCriterionIdTest {

    @Test
    void rejects_null_value() {
        assertThatThrownBy(() -> SortingCriterionId.of(null))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void rejects_blank_value() {
        assertThatThrownBy(() -> SortingCriterionId.of("   "))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void preserves_the_value() {
        assertThat(SortingCriterionId.of("custom_criterion").value()).isEqualTo("custom_criterion");
    }

    @Test
    void exposes_well_known_constants() {
        assertThat(SortingCriterionId.SALES_UNITS.value()).isEqualTo("sales_units");
        assertThat(SortingCriterionId.STOCK_RATIO.value()).isEqualTo("stock_ratio");
    }
}
