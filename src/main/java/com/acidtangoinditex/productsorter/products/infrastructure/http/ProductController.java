package com.acidtangoinditex.productsorter.products.infrastructure.http;

import com.acidtangoinditex.productsorter.products.application.SortProductsUseCase;
import com.acidtangoinditex.productsorter.products.application.SortedProductsResponse;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/products")
public class ProductController {

    private final SortProductsUseCase sortProductsUseCase;

    public ProductController(SortProductsUseCase sortProductsUseCase) {
        this.sortProductsUseCase = sortProductsUseCase;
    }

    @PostMapping("/sorted")
    public SortedProductsResponse sort(@Valid @RequestBody SortProductsHttpRequest request) {
        return sortProductsUseCase.handle(request.toQuery());
    }
}
