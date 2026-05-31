package com.acidtangoinditex.productsorter.products.domain.product.interfaces;

import com.acidtangoinditex.productsorter.products.domain.product.Product;
import com.acidtangoinditex.productsorter.products.domain.product.sorting.Score;

@FunctionalInterface
public interface ScorerInterface {

    Score score(Product product);
}
