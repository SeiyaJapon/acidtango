package com.acidtangoinditex.productsorter.products.application;

import com.acidtangoinditex.productsorter.products.domain.product.ProductMother;
import com.acidtangoinditex.productsorter.products.domain.product.Product;
import com.acidtangoinditex.productsorter.products.domain.product.valueObjects.ProductId;
import com.acidtangoinditex.productsorter.products.domain.product.interfaces.ProductRepositoryInterface;
import com.acidtangoinditex.productsorter.products.domain.product.sorting.CriterionWeights;
import com.acidtangoinditex.productsorter.products.domain.product.sorting.ProductScoringService;
import com.acidtangoinditex.productsorter.products.domain.product.sorting.SalesUnitsCriterion;
import com.acidtangoinditex.productsorter.products.domain.product.sorting.SortingCriteriaCatalog;
import com.acidtangoinditex.productsorter.products.domain.product.sorting.SortingCriterionId;
import com.acidtangoinditex.productsorter.products.domain.product.sorting.StockRatioCriterion;
import com.acidtangoinditex.productsorter.products.domain.product.sorting.Weight;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class SortProductsUseCaseTest {

    private InMemoryProductRepository productRepository;
    private SortProductsUseCase handler;

    @BeforeEach
    void setUp() {
        SortingCriteriaCatalog catalog = new SortingCriteriaCatalog(List.of(new SalesUnitsCriterion(), new StockRatioCriterion()));
        ProductScoringService scoringService = new ProductScoringService(catalog);
        productRepository = new InMemoryProductRepository();
        handler = new SortProductsUseCase(productRepository, scoringService);
    }

    @Test
    void produces_response_items_in_the_same_order_as_the_ranking() {
        productRepository.saveAll(List.of(
                ProductMother.vNeckBasicShirt(),
                ProductMother.contrastingFabric(),
                ProductMother.raisedPrint(),
                ProductMother.pleated(),
                ProductMother.contrastingLace(),
                ProductMother.slogan()
        ));
        SortProductsInput query = new SortProductsInput(CriterionWeights.from(Map.of(
                SortingCriterionId.SALES_UNITS, Weight.of(0.8),
                SortingCriterionId.STOCK_RATIO, Weight.of(0.2)
        )));

        SortedProductsResponse response = handler.handle(query);

        assertThat(response.items()).extracting(SortedProductsResponse.Item::id)
                .containsExactly(5L, 3L, 2L, 1L, 6L, 4L);
        assertThat(response.items().get(0).name()).isEqualTo("CONTRASTING LACE T-SHIRT");
        assertThat(response.items().get(0).score().signum()).isPositive();
    }

    @Test
    void empty_repository_yields_empty_response() {
        SortProductsInput query = new SortProductsInput(CriterionWeights.from(Map.of(
                SortingCriterionId.SALES_UNITS, Weight.of(1.0)
        )));

        SortedProductsResponse response = handler.handle(query);

        assertThat(response.items()).isEmpty();
    }

    private static class InMemoryProductRepository implements ProductRepositoryInterface {

        private final java.util.Map<ProductId, Product> store = new java.util.LinkedHashMap<>();

        @Override
        public List<Product> findAll() {
            return List.copyOf(store.values());
        }

        @Override
        public Optional<Product> findById(ProductId id) {
            return Optional.ofNullable(store.get(id));
        }

        @Override
        public void saveAll(List<Product> products) {
            products.forEach(product -> store.put(product.id(), product));
        }

        @Override
        public long count() {
            return store.size();
        }
    }
}
