package com.acidtangoinditex.productsorter.products.domain.product.sorting;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ScoreTest {

    @Test
    void rejects_negative_value() {
        assertThatThrownBy(() -> Score.of(-0.0001))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void zero_is_a_valid_score() {
        assertThat(Score.ZERO.value()).isEqualByComparingTo(BigDecimal.ZERO);
    }

    @Test
    void plus_adds_two_scores() {
        Score sum = Score.of(0.4).plus(Score.of(0.6));
        assertThat(sum.value()).isEqualByComparingTo(new BigDecimal("1.0"));
    }

    @Test
    void weighted_by_multiplies_score_by_weight() {
        Score weighted = Score.of(0.5).weightedBy(Weight.of(0.4));
        assertThat(weighted.value()).isEqualByComparingTo(new BigDecimal("0.2"));
    }

    @Test
    void weighted_by_zero_yields_zero() {
        Score weighted = Score.of(0.9).weightedBy(Weight.ZERO);
        assertThat(weighted.value()).isEqualByComparingTo(BigDecimal.ZERO);
    }

    @Test
    void ordering_uses_numerical_comparison() {
        assertThat(Score.of(0.5).compareTo(Score.of(0.7))).isNegative();
        assertThat(Score.of(0.7).compareTo(Score.of(0.5))).isPositive();
        assertThat(Score.of(0.7).compareTo(Score.of(0.7))).isZero();
    }
}
