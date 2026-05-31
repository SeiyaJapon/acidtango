package com.acidtangoinditex.productsorter.products.domain.product;

import com.acidtangoinditex.productsorter.products.domain.product.valueObjects.ProductId;
import com.acidtangoinditex.productsorter.products.domain.product.valueObjects.ProductName;
import com.acidtangoinditex.productsorter.products.domain.product.valueObjects.SalesUnits;
import com.acidtangoinditex.productsorter.products.domain.product.valueObjects.StockBySize;

import java.util.Objects;

public final class Product {

    private final ProductId id;
    private final ProductName name;
    private final SalesUnits salesUnits;
    private final StockBySize stockBySize;

    private Product(ProductId id, ProductName name, SalesUnits salesUnits, StockBySize stockBySize) {
        this.id = Objects.requireNonNull(id, "product id is required");
        this.name = Objects.requireNonNull(name, "product name is required");
        this.salesUnits = Objects.requireNonNull(salesUnits, "sales units are required");
        this.stockBySize = Objects.requireNonNull(stockBySize, "stock by size is required");
    }

    public static Product create(ProductId id, ProductName name, SalesUnits salesUnits, StockBySize stockBySize) {
        return new Product(id, name, salesUnits, stockBySize);
    }

    public ProductId id() {
        return id;
    }

    public ProductName name() {
        return name;
    }

    public SalesUnits salesUnits() {
        return salesUnits;
    }

    public StockBySize stockBySize() {
        return stockBySize;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (!(other instanceof Product that)) return false;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public String toString() {
        return "Product{id=" + id + ", name=" + name + '}';
    }
}
