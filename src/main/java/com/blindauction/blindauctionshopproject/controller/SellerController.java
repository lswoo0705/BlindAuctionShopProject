package com.blindauction.blindauctionshopproject.controller;

import com.blindauction.blindauctionshopproject.dto.security.StatusResponse;
import com.blindauction.blindauctionshopproject.dto.seller.*;
import com.blindauction.blindauctionshopproject.dto.seller.ProductRegisterRequest;
import com.blindauction.blindauctionshopproject.dto.seller.ProductUpdateRequest;
import com.blindauction.blindauctionshopproject.dto.seller.SellerProductDetailResponse;
import com.blindauction.blindauctionshopproject.dto.seller.SellerProductResponse;
import com.blindauction.blindauctionshopproject.service.SellerService;
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
import java.util.List;

@RestController
@RequiredArgsConstructor
public class SellerController {
    private final SellerService sellerService;

    // 나의 판매상품 등록
    @PostMapping("/sellers/products")
    public ResponseEntity<StatusResponse> registerProduct(@RequestBody ProductRegisterRequest productRegisterRequest, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        StatusResponse statusResponse = new StatusResponse(HttpStatus.CREATED.value(), "상품 등록 완료");
        String username = userDetails.getUsername();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("application", "json", StandardCharsets.UTF_8));
        sellerService.registerProduct(productRegisterRequest, username);
        return new ResponseEntity<>(statusResponse, headers, HttpStatus.CREATED);
    }

    // 나의 전체 판매상품 조회
    @GetMapping("/sellers/products/list")
    public Page<SellerProductResponse> getSellerProductList(@AuthenticationPrincipal UserDetailsImpl userDetails, @RequestParam int page) {
        String username = userDetails.getUsername();
        return sellerService.getSellerProductList(username, page -1);
    }

    // 나의 개별 판매상품 조회
    @GetMapping("/sellers/products/{productId}")
    public List<SellerProductDetailResponse> getSellerProduct(@AuthenticationPrincipal UserDetailsImpl userDetails, @PathVariable Long productId) {
        String username = userDetails.getUsername();
        return sellerService.getSellerProduct(username, productId);
    }

    // 나의 판매상품 수정
    @PutMapping("/sellers/products/{productId}")
    public ResponseEntity<StatusResponse> updateSellerProduct(@PathVariable Long productId, @RequestBody ProductUpdateRequest productUpdateRequest, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        StatusResponse statusResponse = new StatusResponse(HttpStatus.CREATED.value(), "상품 수정 완료");
        String username = userDetails.getUsername();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("application", "json", StandardCharsets.UTF_8));
        sellerService.updateSellerProduct(productId, productUpdateRequest, username);
        return new ResponseEntity<>(statusResponse, headers, HttpStatus.CREATED);
    }

    // 나의 판매상품 삭제
    @DeleteMapping("/sellers/products/{productId}")
    public ResponseEntity<StatusResponse> deleteSellerProduct(@PathVariable Long productId, @AuthenticationPrincipal UserDetailsImpl userDetails, @RequestBody ProductDeleteRequest productDeleteRequest) {
        StatusResponse statusResponse = new StatusResponse(HttpStatus.CREATED.value(), "상품 삭제 완료");
        String username = userDetails.getUsername();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("application", "json", StandardCharsets.UTF_8));
        sellerService.deleteSellerProduct(productId, username, productDeleteRequest);
        return new ResponseEntity<>(statusResponse, headers, HttpStatus.CREATED);
    }

    //나의 판매자 프로필 조회
    @GetMapping("/sellers/profile")
    public SellerProfileResponse getSellerProfile(@AuthenticationPrincipal UserDetailsImpl userDetails){
        String username = userDetails.getUsername();
        return sellerService.getSellerProfile(username);
    }

    //나의 판매자 프로필 수정
    @PutMapping("/sellers/profile")
    public ResponseEntity<StatusResponse> updateSellerProfile(@RequestBody SellerProfileUpdateRequest sellerProfileUpdateRequest, @AuthenticationPrincipal UserDetailsImpl userDetails){
        StatusResponse statusResponse = new StatusResponse(HttpStatus.OK.value(), "프로필 수정 완료");
        HttpHeaders headers = new HttpHeaders();
        String username = userDetails.getUsername();
        sellerService.updateSellerProfile(sellerProfileUpdateRequest, username);
        headers.setContentType(new MediaType("application", "json", StandardCharsets.UTF_8));
        return new ResponseEntity<>(statusResponse, headers, HttpStatus.OK);
    }

    // 전체상품 고객(구매)요청 목록 조회
    @GetMapping("/sellers/purchase-permission")
    public Page<ProductPurchasePermissionResponse> getPurchasePermissionList(@AuthenticationPrincipal UserDetailsImpl userDetails, @RequestParam int page) {
        String username = userDetails.getUsername();
        return sellerService.getPurchasePermissionList(username, page -1);
    }

    // 고객(거래)요청 수락&거부
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
