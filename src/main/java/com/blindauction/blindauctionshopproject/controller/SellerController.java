package com.blindauction.blindauctionshopproject.controller;

import com.blindauction.blindauctionshopproject.dto.security.StatusResponse;
import com.blindauction.blindauctionshopproject.dto.seller.*;
import com.blindauction.blindauctionshopproject.service.SellerService;
import com.blindauction.blindauctionshopproject.util.security.UserDetailsImpl;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
    public ResponseEntity<StatusResponse> updateSellerProduct(@PathVariable Long productId, @RequestBody ProductUpdateRequest productUpdateRequest) {
        StatusResponse statusResponse = new StatusResponse(HttpStatus.CREATED.value(), "등록 완료");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("application", "json", StandardCharsets.UTF_8));
//        sellerService.updateSellerProduct(productId, productUpdateRequest, user);
        return new ResponseEntity<>(statusResponse, headers, HttpStatus.CREATED);
    }

    //나의 판매자 프로필 설정
    @PutMapping("/sellers/profile")
    public ResponseEntity<StatusResponse> getSellerProfile(@RequestBody SellerProfileUpdateRequest sellerProfileUpdateRequest, @AuthenticationPrincipal UserDetailsImpl userDetails){
        StatusResponse statusResponse = new StatusResponse(HttpStatus.OK.value(), "프로필 수정 완료");
        HttpHeaders headers = new HttpHeaders();
        String username = userDetails.getUsername();
        sellerService.getSellerProfile(sellerProfileUpdateRequest, username);
        headers.setContentType(new MediaType("application", "json", StandardCharsets.UTF_8));
        return new ResponseEntity<>(statusResponse, headers, HttpStatus.OK);
    }


}
