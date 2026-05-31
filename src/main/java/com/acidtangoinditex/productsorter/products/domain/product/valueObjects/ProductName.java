package com.acidtangoinditex.productsorter.products.domain.product.valueObjects;

import java.util.Objects;

public record ProductName(String value) {

    private static final int MAX_LENGTH = 200;

    public ProductName {
        Objects.requireNonNull(value, "product name cannot be null");
        if (value.isBlank()) {
            throw new IllegalArgumentException("product name cannot be blank");
        }
        if (value.length() > MAX_LENGTH) {
            throw new IllegalArgumentException("product name exceeds the maximum length of " + MAX_LENGTH);
        }
    }

    public static ProductName of(String value) {
        return new ProductName(value);
    }
}
