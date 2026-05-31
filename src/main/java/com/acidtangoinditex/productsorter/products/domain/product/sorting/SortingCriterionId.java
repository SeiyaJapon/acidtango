package com.acidtangoinditex.productsorter.products.domain.product.sorting;

import java.util.Objects;

public record SortingCriterionId(String value) {

    public static final SortingCriterionId SALES_UNITS = new SortingCriterionId("sales_units");
    public static final SortingCriterionId STOCK_RATIO = new SortingCriterionId("stock_ratio");

    public SortingCriterionId {
        Objects.requireNonNull(value, "criterion id cannot be null");
        if (value.isBlank()) {
            throw new IllegalArgumentException("criterion id cannot be blank");
        }
    }

    public static SortingCriterionId of(String value) {
        return new SortingCriterionId(value);
    }
}
