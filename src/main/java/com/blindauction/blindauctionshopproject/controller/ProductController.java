package com.blindauction.blindauctionshopproject.controller;

import com.blindauction.blindauctionshopproject.dto.product.ProductDetailResponse;
import com.blindauction.blindauctionshopproject.dto.product.ProductResponse;
import com.blindauction.blindauctionshopproject.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/products")
public class ProductController {
    private final ProductService productService;

    // 전체 판매상품 목록 조회
    @GetMapping("/list")
    public Page<ProductResponse> getProductList(@RequestParam int page) {
        return productService.getProductList(page);
    }

    // 개별 판매상품 상세 조회
    @GetMapping("/{productId}")
    public ProductDetailResponse getProductById(@PathVariable Long productId) {
        return productService.getProductById(productId);
    }
}
