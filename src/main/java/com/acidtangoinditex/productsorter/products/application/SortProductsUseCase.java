package com.acidtangoinditex.productsorter.products.application;

import com.acidtangoinditex.productsorter.products.domain.product.Product;
import com.acidtangoinditex.productsorter.products.domain.product.interfaces.ProductRepositoryInterface;
import com.acidtangoinditex.productsorter.products.domain.product.sorting.ProductScoringService;
import com.acidtangoinditex.productsorter.products.domain.product.sorting.ScoredProduct;

import java.util.List;
import java.util.Objects;

public final class SortProductsUseCase {

    private final ProductRepositoryInterface productRepository;
    private final ProductScoringService productScoringService;

    public SortProductsUseCase(ProductRepositoryInterface productRepository, ProductScoringService scoringService) {
        this.productRepository = Objects.requireNonNull(productRepository, "product repository is required");
        this.productScoringService = Objects.requireNonNull(scoringService, "scoring service is required");
    }

    public SortedProductsResponse handle(SortProductsInput query) {
        Objects.requireNonNull(query, "query is required");
        List<Product> catalog = productRepository.findAll();
        List<ScoredProduct> ranked = productScoringService.rank(catalog, query.weights());
        return new SortedProductsResponse(ranked.stream().map(this::toResponseItem).toList());
    }

    private SortedProductsResponse.Item toResponseItem(ScoredProduct scoredProduct) {
        return new SortedProductsResponse.Item(
                scoredProduct.product().id().value(),
                scoredProduct.product().name().value(),
                scoredProduct.score().value()
        );
    }
}
