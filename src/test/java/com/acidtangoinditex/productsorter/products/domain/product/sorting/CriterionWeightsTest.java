package com.acidtangoinditex.productsorter.products.domain.product.sorting;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class CriterionWeightsTest {

    @Test
    void rejects_empty_weights() {
        assertThatThrownBy(() -> CriterionWeights.from(Map.of()))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void rejects_null_weights() {
        assertThatThrownBy(() -> CriterionWeights.from(null))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void rejects_null_entries() {
        Map<SortingCriterionId, Weight> withNullValue = new HashMap<>();
        withNullValue.put(SortingCriterionId.SALES_UNITS, null);
        assertThatThrownBy(() -> CriterionWeights.from(withNullValue))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void rejects_all_zero_weights() {
        Map<SortingCriterionId, Weight> allZero = Map.of(
                SortingCriterionId.SALES_UNITS, Weight.ZERO,
                SortingCriterionId.STOCK_RATIO, Weight.ZERO
        );
        assertThatThrownBy(() -> CriterionWeights.from(allZero))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void returns_zero_weight_for_unregistered_criterion() {
        CriterionWeights weights = CriterionWeights.from(Map.of(SortingCriterionId.SALES_UNITS, Weight.of(0.5)));
        assertThat(weights.at(SortingCriterionId.STOCK_RATIO).isZero()).isTrue();
    }

    @Test
    void exposes_the_criterion_ids() {
        CriterionWeights weights = CriterionWeights.from(Map.of(
                SortingCriterionId.SALES_UNITS, Weight.of(0.5),
                SortingCriterionId.STOCK_RATIO, Weight.of(0.5)
        ));
        assertThat(weights.criterionIds())
                .containsExactlyInAnyOrder(SortingCriterionId.SALES_UNITS, SortingCriterionId.STOCK_RATIO);
    }

    @Test
    void as_map_exposes_each_weight_under_its_criterion() {
        CriterionWeights weights = CriterionWeights.from(Map.of(
                SortingCriterionId.SALES_UNITS, Weight.of(0.3),
                SortingCriterionId.STOCK_RATIO, Weight.of(0.7)
        ));
        assertThat(weights.asMap())
                .containsEntry(SortingCriterionId.SALES_UNITS, Weight.of(0.3))
                .containsEntry(SortingCriterionId.STOCK_RATIO, Weight.of(0.7))
                .hasSize(2);
    }
}
