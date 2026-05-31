package com.acidtangoinditex.productsorter.products.domain.product.sorting;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public final class CriterionWeights {

    private final Map<SortingCriterionId, Weight> weights;

    private CriterionWeights(Map<SortingCriterionId, Weight> weights) {
        this.weights = Collections.unmodifiableMap(new HashMap<>(weights));
    }

    public static CriterionWeights from(Map<SortingCriterionId, Weight> weights) {
        Objects.requireNonNull(weights, "weights cannot be null");
        if (weights.isEmpty()) {
            throw new IllegalArgumentException("at least one weight must be provided");
        }
        weights.forEach((id, weight) -> {
            if (id == null || weight == null) {
                throw new IllegalArgumentException("weights cannot contain null entries");
            }
        });
        boolean allZero = weights.values().stream().allMatch(Weight::isZero);
        if (allZero) {
            throw new IllegalArgumentException("at least one weight must be greater than zero");
        }
        return new CriterionWeights(weights);
    }

    public Set<SortingCriterionId> criterionIds() {
        return weights.keySet();
    }

    public Weight at(SortingCriterionId id) {
        return weights.getOrDefault(id, Weight.ZERO);
    }

    public Map<SortingCriterionId, Weight> asMap() {
        return weights;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (!(other instanceof CriterionWeights that)) return false;
        return weights.equals(that.weights);
    }

    @Override
    public int hashCode() {
        return weights.hashCode();
    }

    @Override
    public String toString() {
        return "CriterionWeights{" + weights + '}';
    }
}
