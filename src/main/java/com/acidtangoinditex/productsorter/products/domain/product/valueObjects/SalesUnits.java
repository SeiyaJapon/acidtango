package com.acidtangoinditex.productsorter.products.domain.product.valueObjects;

public record SalesUnits(int value) {

    public SalesUnits {
        if (value < 0) {
            throw new IllegalArgumentException("sales units cannot be negative: " + value);
        }
    }

    public static SalesUnits of(int value) {
        return new SalesUnits(value);
    }
}
