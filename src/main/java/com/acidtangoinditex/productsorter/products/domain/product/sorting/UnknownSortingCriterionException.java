package com.acidtangoinditex.productsorter.products.domain.product.sorting;

public class UnknownSortingCriterionException extends RuntimeException {

    private final SortingCriterionId criterionId;

    public UnknownSortingCriterionException(SortingCriterionId criterionId) {
        super("unknown sorting criterion: " + criterionId.value());
        this.criterionId = criterionId;
    }

    public SortingCriterionId criterionId() {
        return criterionId;
    }
}
