package com.acidtangoinditex.productsorter.products.infrastructure.bootstrap;

import com.acidtangoinditex.productsorter.products.application.SortProductsUseCase;
import com.acidtangoinditex.productsorter.products.domain.product.interfaces.ProductRepositoryInterface;
import com.acidtangoinditex.productsorter.products.domain.product.sorting.ProductScoringService;
import com.acidtangoinditex.productsorter.products.domain.product.sorting.SalesUnitsCriterion;
import com.acidtangoinditex.productsorter.products.domain.product.sorting.SortingCriteriaCatalog;
import com.acidtangoinditex.productsorter.products.domain.product.interfaces.SortingCriterionInterface;
import com.acidtangoinditex.productsorter.products.domain.product.sorting.StockRatioCriterion;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SortingConfiguration {

    @Bean
    public SortingCriterionInterface salesUnitsCriterion() {
        return new SalesUnitsCriterion();
    }

    @Bean
    public SortingCriterionInterface stockRatioCriterion() {
        return new StockRatioCriterion();
    }

    @Bean
    public SortingCriteriaCatalog sortingCriteriaCatalog(List<SortingCriterionInterface> criteria) {
        return new SortingCriteriaCatalog(criteria);
    }

    @Bean
    public ProductScoringService productScoringService(SortingCriteriaCatalog catalog) {
        return new ProductScoringService(catalog);
    }

    @Bean
    public SortProductsUseCase sortProductsQueryHandler(ProductRepositoryInterface productRepository,
                                                        ProductScoringService scoringService) {
        return new SortProductsUseCase(productRepository, scoringService);
    }
}
