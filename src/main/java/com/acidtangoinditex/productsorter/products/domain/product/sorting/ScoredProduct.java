package com.acidtangoinditex.productsorter.products.domain.product.sorting;

import com.acidtangoinditex.productsorter.products.domain.product.Product;

import java.util.Objects;

public record ScoredProduct(Product product, Score score) {

    public ScoredProduct {
        Objects.requireNonNull(product, "product is required");
        Objects.requireNonNull(score, "score is required");
    }
}
