package com.acidtangoinditex.productsorter.products.domain.product.sorting;

import com.acidtangoinditex.productsorter.products.domain.product.ProductMother;
import com.acidtangoinditex.productsorter.products.domain.product.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ProductScoringServiceTest {

    private SortingCriteriaCatalog criteriaCatalog;
    private ProductScoringService scoringService;
    private List<Product> pdfCatalog;

    @BeforeEach
    void setUp() {
        criteriaCatalog = new SortingCriteriaCatalog(List.of(new SalesUnitsCriterion(), new StockRatioCriterion()));
        scoringService = new ProductScoringService(criteriaCatalog);
        pdfCatalog = List.of(
                ProductMother.vNeckBasicShirt(),
                ProductMother.contrastingFabric(),
                ProductMother.raisedPrint(),
                ProductMother.pleated(),
                ProductMother.contrastingLace(),
                ProductMother.slogan()
        );
    }

    @Test
    void empty_catalog_yields_empty_ranking() {
        CriterionWeights weights = CriterionWeights.from(Map.of(SortingCriterionId.SALES_UNITS, Weight.of(1.0)));
        assertThat(scoringService.rank(List.of(), weights)).isEmpty();
    }

    @Test
    void ranks_by_sales_units_when_only_sales_weight_is_set() {
        CriterionWeights weights = CriterionWeights.from(Map.of(SortingCriterionId.SALES_UNITS, Weight.of(1.0)));
        List<Long> ranked = idsFrom(scoringService.rank(pdfCatalog, weights));
        assertThat(ranked).containsExactly(5L, 1L, 3L, 2L, 6L, 4L);
    }

    @Test
    void ranks_by_stock_ratio_when_only_stock_weight_is_set() {
        CriterionWeights weights = CriterionWeights.from(Map.of(SortingCriterionId.STOCK_RATIO, Weight.of(1.0)));
        List<Long> ranked = idsFrom(scoringService.rank(pdfCatalog, weights));
        assertThat(ranked).containsExactly(2L, 3L, 4L, 6L, 1L, 5L);
    }

    @Test
    void combines_criteria_using_their_weights() {
        CriterionWeights weights = CriterionWeights.from(Map.of(
                SortingCriterionId.SALES_UNITS, Weight.of(0.8),
                SortingCriterionId.STOCK_RATIO, Weight.of(0.2)
        ));
        List<Long> ranked = idsFrom(scoringService.rank(pdfCatalog, weights));
        assertThat(ranked).containsExactly(5L, 3L, 2L, 1L, 6L, 4L);
    }

    @Test
    void zero_weight_criteria_do_not_contribute_to_the_total_score() {
        CriterionWeights includingZero = CriterionWeights.from(Map.of(
                SortingCriterionId.SALES_UNITS, Weight.of(1.0),
                SortingCriterionId.STOCK_RATIO, Weight.ZERO
        ));
        CriterionWeights salesOnly = CriterionWeights.from(Map.of(SortingCriterionId.SALES_UNITS, Weight.of(1.0)));
        assertThat(idsFrom(scoringService.rank(pdfCatalog, includingZero)))
                .isEqualTo(idsFrom(scoringService.rank(pdfCatalog, salesOnly)));
    }

    @Test
    void ties_break_by_product_id_ascending() {
        List<Product> tied = List.of(
                ProductMother.product(9, "NINE", 10, 1, 1, 1),
                ProductMother.product(2, "TWO",  10, 1, 1, 1),
                ProductMother.product(5, "FIVE", 10, 1, 1, 1)
        );
        CriterionWeights weights = CriterionWeights.from(Map.of(SortingCriterionId.SALES_UNITS, Weight.of(1.0)));
        List<Long> ranked = idsFrom(scoringService.rank(tied, weights));
        assertThat(ranked).containsExactly(2L, 5L, 9L);
    }

    @Test
    void rejects_unknown_criterion_in_the_weights() {
        SortingCriterionId unknown = SortingCriterionId.of("popularity");
        CriterionWeights weights = CriterionWeights.from(Map.of(unknown, Weight.of(0.5)));
        assertThatThrownBy(() -> scoringService.rank(pdfCatalog, weights))
                .isInstanceOf(UnknownSortingCriterionException.class);
    }

    @Test
    void rejects_null_arguments() {
        assertThatThrownBy(() -> scoringService.rank(null, CriterionWeights.from(Map.of(SortingCriterionId.SALES_UNITS, Weight.of(1.0)))))
                .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> scoringService.rank(pdfCatalog, null))
                .isInstanceOf(NullPointerException.class);
    }

    private List<Long> idsFrom(List<ScoredProduct> ranked) {
        return ranked.stream().map(scored -> scored.product().id().value()).toList();
    }
}
