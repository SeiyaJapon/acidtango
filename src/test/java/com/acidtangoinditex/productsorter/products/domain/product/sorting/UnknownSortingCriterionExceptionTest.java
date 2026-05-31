package com.acidtangoinditex.productsorter.products.domain.product.sorting;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class UnknownSortingCriterionExceptionTest {

    @Test
    void exposes_the_offending_criterion_id_and_message() {
        SortingCriterionId offender = SortingCriterionId.of("popularity");
        UnknownSortingCriterionException exception = new UnknownSortingCriterionException(offender);

        assertThat(exception.criterionId()).isEqualTo(offender);
        assertThat(exception.getMessage()).contains("popularity");
    }
}
