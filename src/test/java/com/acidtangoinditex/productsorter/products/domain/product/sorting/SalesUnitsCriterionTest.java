package com.acidtangoinditex.productsorter.products.domain.product.sorting;

import com.acidtangoinditex.productsorter.products.domain.product.ProductMother;
import com.acidtangoinditex.productsorter.products.domain.product.Product;
import com.acidtangoinditex.productsorter.products.domain.product.interfaces.ScorerInterface;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class SalesUnitsCriterionTest {

    private final SalesUnitsCriterion criterion = new SalesUnitsCriterion();

    @Test
    void exposes_its_identifier() {
        assertThat(criterion.id()).isEqualTo(SortingCriterionId.SALES_UNITS);
    }

    @Test
    void normalizes_each_product_against_the_top_seller() {
        List<Product> catalog = List.of(
                ProductMother.product(1, "TOP",    100, 1, 1, 1),
                ProductMother.product(2, "MIDDLE", 50,  1, 1, 1),
                ProductMother.product(3, "TAIL",   0,   1, 1, 1)
        );
        ScorerInterface scorer = criterion.prepareFor(catalog);
        assertThat(scorer.score(catalog.get(0)).value()).isEqualByComparingTo(BigDecimal.ONE);
        assertThat(scorer.score(catalog.get(1)).value()).isEqualByComparingTo(new BigDecimal("0.5"));
        assertThat(scorer.score(catalog.get(2)).value()).isEqualByComparingTo(BigDecimal.ZERO);
    }

    @Test
    void scores_everything_zero_when_no_product_has_sales() {
        List<Product> catalog = List.of(
                ProductMother.product(1, "A", 0, 1, 1, 1),
                ProductMother.product(2, "B", 0, 1, 1, 1)
        );
        ScorerInterface scorer = criterion.prepareFor(catalog);
        assertThat(scorer.score(catalog.get(0)).value()).isEqualByComparingTo(BigDecimal.ZERO);
        assertThat(scorer.score(catalog.get(1)).value()).isEqualByComparingTo(BigDecimal.ZERO);
    }
}
