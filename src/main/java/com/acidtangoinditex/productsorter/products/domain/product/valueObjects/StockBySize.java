package com.acidtangoinditex.productsorter.products.domain.product.valueObjects;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;
import java.util.Objects;

public final class StockBySize {

    private final Map<Size, StockUnits> stockBySize;

    private StockBySize(Map<Size, StockUnits> stockBySize) {
        this.stockBySize = Collections.unmodifiableMap(new EnumMap<>(stockBySize));
    }

    public static StockBySize from(Map<Size, StockUnits> stockBySize) {
        Objects.requireNonNull(stockBySize, "stock by size cannot be null");
        if (stockBySize.isEmpty()) {
            throw new IllegalArgumentException("stock by size cannot be empty");
        }
        stockBySize.forEach((size, units) -> {
            if (size == null || units == null) {
                throw new IllegalArgumentException("stock by size cannot contain null entries");
            }
        });
        return new StockBySize(stockBySize);
    }

    public StockUnits at(Size size) {
        StockUnits units = stockBySize.get(size);
        if (units == null) {
            throw new IllegalArgumentException("no stock entry registered for size " + size);
        }
        return units;
    }

    public Map<Size, StockUnits> asMap() {
        return stockBySize;
    }

    public int sizesWithStock() {
        return (int) stockBySize.values().stream().filter(StockUnits::isAvailable).count();
    }

    public int totalSizes() {
        return stockBySize.size();
    }

    public BigDecimal availabilityRatio() {
        return BigDecimal.valueOf(sizesWithStock())
                .divide(BigDecimal.valueOf(totalSizes()), MathContext.DECIMAL64);
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (!(other instanceof StockBySize that)) return false;
        return stockBySize.equals(that.stockBySize);
    }

    @Override
    public int hashCode() {
        return stockBySize.hashCode();
    }

    @Override
    public String toString() {
        return "StockBySize{" + stockBySize + '}';
    }
}
