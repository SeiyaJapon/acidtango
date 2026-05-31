package com.acidtangoinditex.productsorter.products.domain.product.interfaces;

import com.acidtangoinditex.productsorter.products.domain.product.Product;
import com.acidtangoinditex.productsorter.products.domain.product.sorting.SortingCriterionId;

import java.util.List;

public interface SortingCriterionInterface {

    SortingCriterionId id();

    ScorerInterface prepareFor(List<Product> catalog);
}
