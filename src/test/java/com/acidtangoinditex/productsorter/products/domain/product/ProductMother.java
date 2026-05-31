package com.acidtangoinditex.productsorter.products.domain.product;

import com.acidtangoinditex.productsorter.products.domain.product.Product;
import com.acidtangoinditex.productsorter.products.domain.product.valueObjects.ProductId;
import com.acidtangoinditex.productsorter.products.domain.product.valueObjects.ProductName;
import com.acidtangoinditex.productsorter.products.domain.product.valueObjects.SalesUnits;
import com.acidtangoinditex.productsorter.products.domain.product.valueObjects.Size;
import com.acidtangoinditex.productsorter.products.domain.product.valueObjects.StockBySize;
import com.acidtangoinditex.productsorter.products.domain.product.valueObjects.StockUnits;

import java.util.EnumMap;
import java.util.Map;

public final class ProductMother {

    private ProductMother() {
    }

    public static Product product(long id, String name, int salesUnits, int small, int medium, int large) {
        Map<Size, StockUnits> stock = new EnumMap<>(Size.class);
        stock.put(Size.S, StockUnits.of(small));
        stock.put(Size.M, StockUnits.of(medium));
        stock.put(Size.L, StockUnits.of(large));
        return Product.create(
                ProductId.of(id),
                ProductName.of(name),
                SalesUnits.of(salesUnits),
                StockBySize.from(stock)
        );
    }

    public static Product vNeckBasicShirt() {
        return product(1, "V-NECH BASIC SHIRT", 100, 4, 9, 0);
    }

    public static Product contrastingFabric() {
        return product(2, "CONTRASTING FABRIC T-SHIRT", 50, 35, 9, 9);
    }

    public static Product raisedPrint() {
        return product(3, "RAISED PRINT T-SHIRT", 80, 20, 2, 20);
    }

    public static Product pleated() {
        return product(4, "PLEATED T-SHIRT", 3, 25, 30, 10);
    }

    public static Product contrastingLace() {
        return product(5, "CONTRASTING LACE T-SHIRT", 650, 0, 1, 0);
    }

    public static Product slogan() {
        return product(6, "SLOGAN T-SHIRT", 20, 9, 2, 5);
    }
}
