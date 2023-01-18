package com.blindauction.blindauctionshopproject.controller;

import com.blindauction.blindauctionshopproject.dto.StatusResponse;
import com.blindauction.blindauctionshopproject.dto.seller.ProductRegisterRequest;
import com.blindauction.blindauctionshopproject.dto.seller.SellerProductResponse;
import com.blindauction.blindauctionshopproject.service.SellerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

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
}
