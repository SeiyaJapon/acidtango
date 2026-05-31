package com.acidtangoinditex.productsorter.products.infrastructure.persistence;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface SpringDataProductRepository extends MongoRepository<ProductDocument, Long> {
}
