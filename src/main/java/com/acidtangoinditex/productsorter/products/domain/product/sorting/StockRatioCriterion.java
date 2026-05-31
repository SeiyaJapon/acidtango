package com.acidtangoinditex.productsorter.products.domain.product.sorting;

import com.acidtangoinditex.productsorter.products.domain.product.Product;
import com.acidtangoinditex.productsorter.products.domain.product.interfaces.ScorerInterface;
import com.acidtangoinditex.productsorter.products.domain.product.interfaces.SortingCriterionInterface;

import java.util.List;

public final class StockRatioCriterion implements SortingCriterionInterface {

    @Override
    public SortingCriterionId id() {
        return SortingCriterionId.STOCK_RATIO;
    }

    @Override
    public ScorerInterface prepareFor(List<Product> catalog) {
        return product -> Score.of(product.stockBySize().availabilityRatio());
    }
}
