package com.acidtangoinditex.productsorter.products.infrastructure.persistence;

import com.acidtangoinditex.productsorter.products.domain.product.ProductMother;
import com.acidtangoinditex.productsorter.products.domain.product.Product;
import com.acidtangoinditex.productsorter.products.domain.product.valueObjects.ProductId;
import com.acidtangoinditex.productsorter.products.domain.product.interfaces.ProductRepositoryInterface;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Import;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataMongoTest
@Testcontainers
@Import(MongoProductRepository.class)
class MongoProductRepositoryIntegrationTest {

    @Container
    @ServiceConnection
    static MongoDBContainer mongo = new MongoDBContainer("mongo:7.0");

    @Autowired
    private ProductRepositoryInterface productRepository;

    @Autowired
    private SpringDataProductRepository springDataRepository;

    @AfterEach
    void cleanUp() {
        springDataRepository.deleteAll();
    }

    @Test
    void saves_and_loads_a_product_preserving_all_attributes() {
        Product original = ProductMother.contrastingFabric();
        productRepository.saveAll(List.of(original));

        Optional<Product> loaded = productRepository.findById(original.id());

        assertThat(loaded).isPresent();
        Product roundTripped = loaded.get();
        assertThat(roundTripped.id()).isEqualTo(original.id());
        assertThat(roundTripped.name()).isEqualTo(original.name());
        assertThat(roundTripped.salesUnits()).isEqualTo(original.salesUnits());
        assertThat(roundTripped.stockBySize()).isEqualTo(original.stockBySize());
    }

    @Test
    void find_all_returns_every_persisted_product() {
        productRepository.saveAll(List.of(
                ProductMother.vNeckBasicShirt(),
                ProductMother.pleated(),
                ProductMother.slogan()
        ));

        List<Product> all = productRepository.findAll();

        assertThat(all).extracting(Product::id)
                .containsExactlyInAnyOrder(ProductId.of(1), ProductId.of(4), ProductId.of(6));
    }

    @Test
    void find_by_id_returns_empty_when_no_match() {
        Optional<Product> loaded = productRepository.findById(ProductId.of(999));
        assertThat(loaded).isEmpty();
    }

    @Test
    void count_reports_the_number_of_persisted_products() {
        assertThat(productRepository.count()).isZero();
        productRepository.saveAll(List.of(ProductMother.vNeckBasicShirt()));
        assertThat(productRepository.count()).isEqualTo(1L);
    }
}
