package com.acidtangoinditex.productsorter.products.infrastructure.bootstrap;

import com.acidtangoinditex.productsorter.products.domain.product.Product;
import com.acidtangoinditex.productsorter.products.domain.product.valueObjects.ProductId;
import com.acidtangoinditex.productsorter.products.domain.product.valueObjects.ProductName;
import com.acidtangoinditex.productsorter.products.domain.product.valueObjects.SalesUnits;
import com.acidtangoinditex.productsorter.products.domain.product.valueObjects.Size;
import com.acidtangoinditex.productsorter.products.domain.product.valueObjects.StockBySize;
import com.acidtangoinditex.productsorter.products.domain.product.valueObjects.StockUnits;

import java.util.EnumMap;
import java.util.Map;

record SeedProductRecord(long id, String name, int salesUnits, Map<String, Integer> stock) {

    Product toDomain() {
        Map<Size, StockUnits> stockBySize = new EnumMap<>(Size.class);
        stock.forEach((sizeName, units) -> stockBySize.put(Size.valueOf(sizeName), StockUnits.of(units)));
        return Product.create(
                ProductId.of(id),
                ProductName.of(name),
                SalesUnits.of(salesUnits),
                StockBySize.from(stockBySize)
        );
    }
}
