package com.acidtangoinditex.productsorter.products.infrastructure.http;

import com.acidtangoinditex.productsorter.products.application.SortProductsInput;
import com.acidtangoinditex.productsorter.products.domain.product.sorting.CriterionWeights;
import com.acidtangoinditex.productsorter.products.domain.product.sorting.SortingCriterionId;
import com.acidtangoinditex.productsorter.products.domain.product.sorting.Weight;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public record SortProductsHttpRequest(@NotNull @NotEmpty Map<String, BigDecimal> weights) {

    public SortProductsInput toQuery() {
        Map<SortingCriterionId, Weight> domainWeights = new HashMap<>();
        weights.forEach((id, value) -> domainWeights.put(SortingCriterionId.of(id), Weight.of(value)));
        return new SortProductsInput(CriterionWeights.from(domainWeights));
    }
}
