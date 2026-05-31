package com.acidtangoinditex.productsorter.products.domain.product.valueObjects;

public record StockUnits(int value) {

    public StockUnits {
        if (value < 0) {
            throw new IllegalArgumentException("stock units cannot be negative: " + value);
        }
    }

    public static StockUnits of(int value) {
        return new StockUnits(value);
    }

    public boolean isAvailable() {
        return value > 0;
    }
}
