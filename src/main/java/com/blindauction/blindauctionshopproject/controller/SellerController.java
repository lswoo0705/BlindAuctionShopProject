package com.blindauction.blindauctionshopproject.controller;

import com.blindauction.blindauctionshopproject.dto.StatusResponse;
import com.blindauction.blindauctionshopproject.dto.seller.ProductRegisterRequest;
import com.blindauction.blindauctionshopproject.dto.seller.ProductUpdateRequest;
import com.blindauction.blindauctionshopproject.dto.seller.SellerProductDetailResponse;
import com.blindauction.blindauctionshopproject.dto.seller.SellerProductResponse;
import com.blindauction.blindauctionshopproject.entity.User;
import com.blindauction.blindauctionshopproject.service.SellerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.StandardCharsets;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class SellerController {
    private final SellerService sellerService;

    // 나의 판매상품 등록
    @PostMapping("/sellers/products")
    public ResponseEntity<StatusResponse> registerProduct(@RequestBody ProductRegisterRequest productRegisterRequest) {
        StatusResponse statusResponse = new StatusResponse(HttpStatus.CREATED.value(), "등록 완료");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("application", "json", StandardCharsets.UTF_8));
        sellerService.registerProduct(productRegisterRequest);
        return new ResponseEntity<>(statusResponse, headers, HttpStatus.CREATED);
    }

    // 나의 전체 판매상품 조회
    @GetMapping("/sellers/products/list")
    public List<SellerProductResponse> getSellerProductList() {
        return sellerService.getSellerProductList();
    }

    // 나의 개별 판매상품 조회
    @GetMapping("/sellers/products/{productId}")
    public SellerProductDetailResponse getSellerProduct(@PathVariable Long productId) {
        return sellerService.getSellerProduct(productId);
    }

    // 나의 판매상품 수정
    @PutMapping("/sellers/products/{productId}")
    public ResponseEntity<StatusResponse> updateSellerProduct(@PathVariable Long productId, @RequestBody ProductUpdateRequest productUpdateRequest, User user) {
        StatusResponse statusResponse = new StatusResponse(HttpStatus.CREATED.value(), "수정 완료");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("application", "json", StandardCharsets.UTF_8));
        sellerService.updateSellerProduct(productId, productUpdateRequest, user);
        return new ResponseEntity<>(statusResponse, headers, HttpStatus.CREATED);
    }

    // 나의 판매상품 삭제
    @DeleteMapping("/sellers/products/{productId}")
    public ResponseEntity<StatusResponse> deleteSellerProduct(@PathVariable Long productId, User user) {
        StatusResponse statusResponse = new StatusResponse(HttpStatus.CREATED.value(), "삭제 완료");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("application", "json", StandardCharsets.UTF_8));
        sellerService.deleteSellerProduct(productId, user);
        return new ResponseEntity<>(statusResponse, headers, HttpStatus.CREATED);
    }
}
