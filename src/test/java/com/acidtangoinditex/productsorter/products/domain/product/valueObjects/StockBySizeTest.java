package com.acidtangoinditex.productsorter.products.domain.product.valueObjects;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class StockBySizeTest {

    @Test
    void rejects_empty_map() {
        assertThatThrownBy(() -> StockBySize.from(Map.of()))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void rejects_null_map() {
        assertThatThrownBy(() -> StockBySize.from(null))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void rejects_null_values() {
        Map<Size, StockUnits> stock = new HashMap<>();
        stock.put(Size.S, null);
        assertThatThrownBy(() -> StockBySize.from(stock))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void counts_sizes_with_stock() {
        StockBySize stock = stockFor(4, 0, 9);
        assertThat(stock.sizesWithStock()).isEqualTo(2);
    }

    @Test
    void total_sizes_includes_zero_stock_entries() {
        StockBySize stock = stockFor(4, 0, 9);
        assertThat(stock.totalSizes()).isEqualTo(3);
    }

    @Test
    void availability_ratio_is_sizes_with_stock_divided_by_total() {
        StockBySize stock = stockFor(4, 0, 9);
        assertThat(stock.availabilityRatio()).isEqualByComparingTo(new BigDecimal("0.6666666666666667"));
    }

    @Test
    void availability_ratio_is_one_when_all_sizes_have_stock() {
        StockBySize stock = stockFor(1, 1, 1);
        assertThat(stock.availabilityRatio()).isEqualByComparingTo(BigDecimal.ONE);
    }

    @Test
    void availability_ratio_is_zero_when_no_sizes_have_stock() {
        StockBySize stock = stockFor(0, 0, 0);
        assertThat(stock.availabilityRatio()).isEqualByComparingTo(BigDecimal.ZERO);
    }

    @Test
    void at_returns_the_stock_for_the_given_size() {
        StockBySize stock = stockFor(4, 9, 0);
        assertThat(stock.at(Size.M)).isEqualTo(StockUnits.of(9));
    }

    @Test
    void at_throws_when_size_is_not_registered() {
        Map<Size, StockUnits> partial = new EnumMap<>(Size.class);
        partial.put(Size.S, StockUnits.of(1));
        StockBySize stock = StockBySize.from(partial);
        assertThatThrownBy(() -> stock.at(Size.L))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void as_map_exposes_the_original_entries() {
        StockBySize stock = stockFor(1, 2, 3);
        assertThat(stock.asMap())
                .containsEntry(Size.S, StockUnits.of(1))
                .containsEntry(Size.M, StockUnits.of(2))
                .containsEntry(Size.L, StockUnits.of(3))
                .hasSize(3);
    }

    @Test
    void as_map_returns_an_immutable_view() {
        StockBySize stock = stockFor(1, 2, 3);
        assertThatThrownBy(() -> stock.asMap().put(Size.S, StockUnits.of(99)))
                .isInstanceOf(UnsupportedOperationException.class);
    }

    private StockBySize stockFor(int small, int medium, int large) {
        Map<Size, StockUnits> stock = new EnumMap<>(Size.class);
        stock.put(Size.S, StockUnits.of(small));
        stock.put(Size.M, StockUnits.of(medium));
        stock.put(Size.L, StockUnits.of(large));
        return StockBySize.from(stock);
    }
}
