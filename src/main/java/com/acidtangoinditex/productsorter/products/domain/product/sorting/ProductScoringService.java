package com.acidtangoinditex.productsorter.products.domain.product.sorting;

import com.acidtangoinditex.productsorter.products.domain.product.Product;
import com.acidtangoinditex.productsorter.products.domain.product.interfaces.ScorerInterface;
import com.acidtangoinditex.productsorter.products.domain.product.interfaces.SortingCriterionInterface;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public final class ProductScoringService {

    private final SortingCriteriaCatalog criteriaCatalog;

    public ProductScoringService(SortingCriteriaCatalog criteriaCatalog) {
        this.criteriaCatalog = Objects.requireNonNull(criteriaCatalog, "criteria catalog is required");
    }

    public List<ScoredProduct> rank(List<Product> catalog, CriterionWeights weights) {
        Objects.requireNonNull(catalog, "catalog is required");
        Objects.requireNonNull(weights, "weights are required");
        if (catalog.isEmpty()) {
            return List.of();
        }

        Map<SortingCriterionId, ScorerInterface> scorersByCriterion = scorersFor(catalog, weights);
        List<ScoredProduct> scored = scoreEachProduct(catalog, weights, scorersByCriterion);
        return sortByScoreDescending(scored);
    }

    private Map<SortingCriterionId, ScorerInterface> scorersFor(List<Product> catalog, CriterionWeights weights) {
        Map<SortingCriterionId, ScorerInterface> scorers = new LinkedHashMap<>();
        for (SortingCriterionId id : weights.criterionIds()) {
            if (weights.at(id).isZero()) {
                continue;
            }
            SortingCriterionInterface criterion = criteriaCatalog.find(id)
                    .orElseThrow(() -> new UnknownSortingCriterionException(id));
            scorers.put(id, criterion.prepareFor(catalog));
        }
        return scorers;
    }

    private List<ScoredProduct> scoreEachProduct(
            List<Product> catalog,
            CriterionWeights weights,
            Map<SortingCriterionId, ScorerInterface> scorersByCriterion
    ) {
        List<ScoredProduct> scored = new ArrayList<>(catalog.size());
        for (Product product : catalog) {
            scored.add(new ScoredProduct(product, totalScoreFor(product, weights, scorersByCriterion)));
        }
        return scored;
    }

    private Score totalScoreFor(
            Product product,
            CriterionWeights weights,
            Map<SortingCriterionId, ScorerInterface> scorersByCriterion
    ) {
        Score total = Score.ZERO;
        for (Map.Entry<SortingCriterionId, ScorerInterface> entry : scorersByCriterion.entrySet()) {
            Score raw = entry.getValue().score(product);
            total = total.plus(raw.weightedBy(weights.at(entry.getKey())));
        }
        return total;
    }

    private List<ScoredProduct> sortByScoreDescending(List<ScoredProduct> scored) {
        Comparator<ScoredProduct> byScoreDescending = Comparator
                .comparing(ScoredProduct::score, Comparator.reverseOrder())
                .thenComparing(scoredProduct -> scoredProduct.product().id().value());
        return scored.stream().sorted(byScoreDescending).toList();
    }
}
