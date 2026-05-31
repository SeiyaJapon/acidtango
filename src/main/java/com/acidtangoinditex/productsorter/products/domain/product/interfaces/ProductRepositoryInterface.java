package com.acidtangoinditex.productsorter.products.domain.product.interfaces;

import com.acidtangoinditex.productsorter.products.domain.product.Product;
import com.acidtangoinditex.productsorter.products.domain.product.valueObjects.ProductId;

import java.util.List;
import java.util.Optional;

public interface ProductRepositoryInterface {

    List<Product> findAll();

    Optional<Product> findById(ProductId id);

    void saveAll(List<Product> products);

    long count();
}
