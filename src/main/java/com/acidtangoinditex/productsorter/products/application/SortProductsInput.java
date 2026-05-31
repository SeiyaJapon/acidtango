package com.acidtangoinditex.productsorter.products.application;

import com.acidtangoinditex.productsorter.products.domain.product.sorting.CriterionWeights;

import java.util.Objects;

public record SortProductsInput(CriterionWeights weights) {

    public SortProductsInput {
        Objects.requireNonNull(weights, "weights are required");
    }
}
