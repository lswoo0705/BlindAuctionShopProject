package com.blindauction.blindauctionshopproject.controller;

import com.blindauction.blindauctionshopproject.dto.product.ProductDetailResponse;
import com.blindauction.blindauctionshopproject.dto.product.ProductResponse;
import com.blindauction.blindauctionshopproject.dto.product.PurchaseRequest;
import com.blindauction.blindauctionshopproject.dto.security.StatusResponse;
import com.blindauction.blindauctionshopproject.service.ProductService;
import com.blindauction.blindauctionshopproject.util.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.StandardCharsets;

@RestController
@RequiredArgsConstructor
@RequestMapping("/products")
public class ProductController {
    private final ProductService productService;

    // 전체 판매상품 목록 조회
    @GetMapping("/list")
    public Page<ProductResponse> getProductList(@RequestParam int page) {
        return productService.getProductList(page - 1);
    }

    // 개별 판매상품 상세 조회
    @GetMapping("/{productId}")
    public ProductDetailResponse getProductById(@PathVariable Long productId) {
        return productService.getProductById(productId);
    }

    // 판매자에게 구입 요청
    @PostMapping("/{productId}")
    public ResponseEntity<StatusResponse> sendPurchasePermission(@RequestBody PurchaseRequest purchaseRequest,
                                                                 @AuthenticationPrincipal UserDetailsImpl userDetails,
                                                                 @PathVariable Long productId) {
        String msg = purchaseRequest.getMsg();
        Long price = purchaseRequest.getPrice();

        String username = userDetails.getUsername();

        StatusResponse statusResponse = new StatusResponse(HttpStatus.CREATED.value(), "거래 요청 완료");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("application", "json", StandardCharsets.UTF_8));

        productService.sendPurchasePermission(msg, price, username, productId);

        return new ResponseEntity<>(statusResponse, headers, HttpStatus.CREATED);
    }
}
