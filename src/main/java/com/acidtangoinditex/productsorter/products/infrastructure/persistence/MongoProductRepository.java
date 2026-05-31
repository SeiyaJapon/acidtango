package com.acidtangoinditex.productsorter.products.infrastructure.persistence;

import com.acidtangoinditex.productsorter.products.domain.product.Product;
import com.acidtangoinditex.productsorter.products.domain.product.valueObjects.ProductId;
import com.acidtangoinditex.productsorter.products.domain.product.interfaces.ProductRepositoryInterface;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class MongoProductRepository implements ProductRepositoryInterface {

    private final SpringDataProductRepository springDataRepository;

    public MongoProductRepository(SpringDataProductRepository springDataRepository) {
        this.springDataRepository = springDataRepository;
    }

    @Override
    public List<Product> findAll() {
        return springDataRepository.findAll().stream()
                .map(ProductDocument::toDomain)
                .toList();
    }

    @Override
    public Optional<Product> findById(ProductId id) {
        return springDataRepository.findById(id.value()).map(ProductDocument::toDomain);
    }

    @Override
    public void saveAll(List<Product> products) {
        List<ProductDocument> documents = products.stream()
                .map(ProductDocument::fromDomain)
                .toList();
        springDataRepository.saveAll(documents);
    }

    @Override
    public long count() {
        return springDataRepository.count();
    }
}
