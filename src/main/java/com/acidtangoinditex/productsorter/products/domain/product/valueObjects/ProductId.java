package com.acidtangoinditex.productsorter.products.domain.product.valueObjects;

public record ProductId(long value) {

    public ProductId {
        if (value <= 0) {
            throw new IllegalArgumentException("product id must be a positive number: " + value);
        }
    }

    public static ProductId of(long value) {
        return new ProductId(value);
    }
}
