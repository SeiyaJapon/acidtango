package com.acidtangoinditex.productsorter.products.domain.product.sorting;

import com.acidtangoinditex.productsorter.products.domain.product.Product;
import com.acidtangoinditex.productsorter.products.domain.product.interfaces.ScorerInterface;
import com.acidtangoinditex.productsorter.products.domain.product.interfaces.SortingCriterionInterface;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.List;

public final class SalesUnitsCriterion implements SortingCriterionInterface {

    @Override
    public SortingCriterionId id() {
        return SortingCriterionId.SALES_UNITS;
    }

    @Override
    public ScorerInterface prepareFor(List<Product> catalog) {
        BigDecimal maxSales = maximumSalesIn(catalog);
        if (maxSales.signum() == 0) {
            return product -> Score.ZERO;
        }
        return product -> normalizedScoreFor(product, maxSales);
    }

    private BigDecimal maximumSalesIn(List<Product> catalog) {
        return catalog.stream()
                .map(product -> BigDecimal.valueOf(product.salesUnits().value()))
                .max(BigDecimal::compareTo)
                .orElse(BigDecimal.ZERO);
    }

    private Score normalizedScoreFor(Product product, BigDecimal maxSales) {
        BigDecimal sales = BigDecimal.valueOf(product.salesUnits().value());
        return Score.of(sales.divide(maxSales, MathContext.DECIMAL64));
    }
}
