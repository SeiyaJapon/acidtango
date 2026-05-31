package com.acidtangoinditex.productsorter.products.infrastructure.persistence;

import com.acidtangoinditex.productsorter.products.domain.product.Product;
import com.acidtangoinditex.productsorter.products.domain.product.valueObjects.ProductId;
import com.acidtangoinditex.productsorter.products.domain.product.valueObjects.ProductName;
import com.acidtangoinditex.productsorter.products.domain.product.valueObjects.SalesUnits;
import com.acidtangoinditex.productsorter.products.domain.product.valueObjects.Size;
import com.acidtangoinditex.productsorter.products.domain.product.valueObjects.StockBySize;
import com.acidtangoinditex.productsorter.products.domain.product.valueObjects.StockUnits;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

@Document(collection = "products")
public class ProductDocument {

    @Id
    private long id;
    private String name;
    private int salesUnits;
    private Map<String, Integer> stockBySize;

    public ProductDocument() {
    }

    public ProductDocument(long id, String name, int salesUnits, Map<String, Integer> stockBySize) {
        this.id = id;
        this.name = name;
        this.salesUnits = salesUnits;
        this.stockBySize = stockBySize;
    }

    public static ProductDocument fromDomain(Product product) {
        Map<String, Integer> stock = new HashMap<>();
        product.stockBySize().asMap().forEach((size, units) -> stock.put(size.name(), units.value()));
        return new ProductDocument(
                product.id().value(),
                product.name().value(),
                product.salesUnits().value(),
                stock
        );
    }

    public Product toDomain() {
        Map<Size, StockUnits> stock = new EnumMap<>(Size.class);
        stockBySize.forEach((sizeName, units) -> stock.put(Size.valueOf(sizeName), StockUnits.of(units)));
        return Product.create(
                ProductId.of(id),
                ProductName.of(name),
                SalesUnits.of(salesUnits),
                StockBySize.from(stock)
        );
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSalesUnits() {
        return salesUnits;
    }

    public void setSalesUnits(int salesUnits) {
        this.salesUnits = salesUnits;
    }

    public Map<String, Integer> getStockBySize() {
        return stockBySize;
    }

    public void setStockBySize(Map<String, Integer> stockBySize) {
        this.stockBySize = stockBySize;
    }
}
