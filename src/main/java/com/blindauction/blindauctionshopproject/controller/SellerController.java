package com.blindauction.blindauctionshopproject.controller;


import com.blindauction.blindauctionshopproject.dto.security.StatusResponse;
import com.blindauction.blindauctionshopproject.dto.seller.*;
import com.blindauction.blindauctionshopproject.dto.seller.ProductRegisterRequest;
import com.blindauction.blindauctionshopproject.dto.seller.ProductUpdateRequest;
import com.blindauction.blindauctionshopproject.dto.seller.SellerProductDetailResponse;
import com.blindauction.blindauctionshopproject.dto.seller.SellerProductResponse;
import com.blindauction.blindauctionshopproject.entity.User;
import com.blindauction.blindauctionshopproject.service.SellerService;
import com.blindauction.blindauctionshopproject.util.security.UserDetailsImpl;
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

    // 조회는 굳이 유저네임을 확인할 필요가 없으니까  (@AuthenticationPrincipal UserDetailsImpl userDetails) 파라미터를 안 받아도 됨, 나머지는 받아야만

    // 나의 판매상품 등록 [확인ㅇ]
    @PostMapping("/sellers/products")
    public ResponseEntity<StatusResponse> registerProduct(@RequestBody ProductRegisterRequest productRegisterRequest, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        StatusResponse statusResponse = new StatusResponse(HttpStatus.CREATED.value(), "등록 완료");
        String username = userDetails.getUsername();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("application", "json", StandardCharsets.UTF_8));
        sellerService.registerProduct(productRegisterRequest, username);
        return new ResponseEntity<>(statusResponse, headers, HttpStatus.CREATED);
    }

    // 나의 전체 판매상품 조회 // 페이징 필요
    @GetMapping("/sellers/products/list")
    public List<SellerProductResponse> getSellerProductList(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return sellerService.getSellerProductList(userDetails);
    }

    // 나의 개별 판매상품 조회
    @GetMapping("/sellers/products/{productId}")
    public List<SellerProductDetailResponse> getSellerProduct(@PathVariable Long productId) {
        return sellerService.getSellerProduct(productId);
    }

    // 나의 판매상품 수정 [확인ㅇ]
    @PutMapping("/sellers/products/{productId}")
    public ResponseEntity<StatusResponse> updateSellerProduct(@PathVariable Long productId, @RequestBody ProductUpdateRequest productUpdateRequest, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        StatusResponse statusResponse = new StatusResponse(HttpStatus.CREATED.value(), "수정 완료");
        String username = userDetails.getUsername();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("application", "json", StandardCharsets.UTF_8));
        sellerService.updateSellerProduct(productId, productUpdateRequest, username);
        return new ResponseEntity<>(statusResponse, headers, HttpStatus.CREATED);
    }

    // 나의 판매상품 삭제  // password 확인 필요
    @DeleteMapping("/sellers/products/{productId}")
    public ResponseEntity<StatusResponse> deleteSellerProduct(@PathVariable Long productId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        StatusResponse statusResponse = new StatusResponse(HttpStatus.CREATED.value(), "상품 삭제 완료");
        String username = userDetails.getUsername();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("application", "json", StandardCharsets.UTF_8));
        sellerService.deleteSellerProduct(productId, username);
        return new ResponseEntity<>(statusResponse, headers, HttpStatus.CREATED);
    }

    //나의 판매자 프로필 수정
    @PutMapping("/sellers/profile")
    public ResponseEntity<StatusResponse> getSellerProfile(@RequestBody SellerProfileUpdateRequest sellerProfileUpdateRequest, @AuthenticationPrincipal UserDetailsImpl userDetails){
        StatusResponse statusResponse = new StatusResponse(HttpStatus.OK.value(), "프로필 수정 완료");
        HttpHeaders headers = new HttpHeaders();
        String username = userDetails.getUsername();
        sellerService.getSellerProfile(sellerProfileUpdateRequest, username);
        headers.setContentType(new MediaType("application", "json", StandardCharsets.UTF_8));
        return new ResponseEntity<>(statusResponse, headers, HttpStatus.OK);
    }

    // 전체상품 고객(구매)요청 목록 조회
    @GetMapping("/sellers/purchase-permission")
    public List<ProductPurchasePermissionResponse> getPurchasePermissionList() {
        return sellerService.getPurchasePermissionList();
    }

    // 고객(거래)요청 수락&완료  // 작업중 // 수락 or 거부를 여기서 판별?
    @PutMapping("/sellers/purchase-permission/{permissionId}")
    public ResponseEntity<StatusResponse> updatePurchasePermission(@PathVariable Long permissionId, @RequestBody PurchasePermissionUpdateRequest purchasePermissionUpdateRequest, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        StatusResponse statusResponse = new StatusResponse(HttpStatus.OK.value(), "구매 요청 처리 완료");
        HttpHeaders headers = new HttpHeaders();
        String username = userDetails.getUsername();
        headers.setContentType(new MediaType("application", "json", StandardCharsets.UTF_8));
        sellerService.updatePurchasePermission(permissionId, purchasePermissionUpdateRequest, username);
        return new ResponseEntity<>(statusResponse, headers, HttpStatus.OK);
    }
}
