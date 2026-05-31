package com.acidtangoinditex.productsorter.products.application;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

public record SortedProductsResponse(List<Item> items) {

    public SortedProductsResponse {
        Objects.requireNonNull(items, "items cannot be null");
        items = List.copyOf(items);
    }

    public record Item(long id, String name, BigDecimal score) {

        public Item {
            Objects.requireNonNull(name, "name cannot be null");
            Objects.requireNonNull(score, "score cannot be null");
        }
    }
}
