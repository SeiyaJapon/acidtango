package com.acidtangoinditex.productsorter.products.infrastructure.bootstrap;

import com.acidtangoinditex.productsorter.products.domain.product.Product;
import com.acidtangoinditex.productsorter.products.domain.product.interfaces.ProductRepositoryInterface;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Component
public class ProductCatalogSeeder implements CommandLineRunner {

    private static final Logger LOG = LoggerFactory.getLogger(ProductCatalogSeeder.class);
    private static final String SEED_LOCATION = "seed/products.json";

    private final ProductRepositoryInterface productRepository;
    private final ObjectMapper objectMapper;

    public ProductCatalogSeeder(ProductRepositoryInterface productRepository, ObjectMapper objectMapper) {
        this.productRepository = productRepository;
        this.objectMapper = objectMapper;
    }

    @Override
    public void run(String... args) {
        if (productRepository.count() > 0) {
            LOG.info("Product catalog already populated; skipping seed");
            return;
        }
        List<Product> products = loadSeedProducts();
        productRepository.saveAll(products);
        LOG.info("Seeded product catalog with {} products", products.size());
    }

    private List<Product> loadSeedProducts() {
        ClassPathResource resource = new ClassPathResource(SEED_LOCATION);
        try (InputStream in = resource.getInputStream()) {
            List<SeedProductRecord> records = objectMapper.readValue(in, new TypeReference<>() {});
            return records.stream().map(SeedProductRecord::toDomain).toList();
        } catch (IOException ioException) {
            throw new IllegalStateException("Unable to load product seed from " + SEED_LOCATION, ioException);
        }
    }
}
