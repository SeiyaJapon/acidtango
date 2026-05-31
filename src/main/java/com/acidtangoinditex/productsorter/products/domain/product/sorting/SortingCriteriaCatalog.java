package com.acidtangoinditex.productsorter.products.domain.product.sorting;

import com.acidtangoinditex.productsorter.products.domain.product.interfaces.SortingCriterionInterface;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public final class SortingCriteriaCatalog {

    private final Map<SortingCriterionId, SortingCriterionInterface> criteriaById;

    public SortingCriteriaCatalog(Collection<SortingCriterionInterface> criteria) {
        Objects.requireNonNull(criteria, "criteria collection cannot be null");
        Map<SortingCriterionId, SortingCriterionInterface> indexed = new LinkedHashMap<>();
        for (SortingCriterionInterface criterion : criteria) {
            Objects.requireNonNull(criterion, "criterion cannot be null");
            if (indexed.put(criterion.id(), criterion) != null) {
                throw new IllegalArgumentException("duplicate sorting criterion: " + criterion.id());
            }
        }
        this.criteriaById = Collections.unmodifiableMap(new LinkedHashMap<>(indexed));
    }

    public Optional<SortingCriterionInterface> find(SortingCriterionId id) {
        return Optional.ofNullable(criteriaById.get(id));
    }

    public List<SortingCriterionInterface> all() {
        return List.copyOf(criteriaById.values());
    }
}
