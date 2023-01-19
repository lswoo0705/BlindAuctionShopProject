package com.blindauction.blindauctionshopproject.service;

import com.blindauction.blindauctionshopproject.dto.seller.*;
import com.blindauction.blindauctionshopproject.entity.Product;
import com.blindauction.blindauctionshopproject.entity.PurchasePermission;
import com.blindauction.blindauctionshopproject.entity.User;
import com.blindauction.blindauctionshopproject.repository.ProductRepository;
import com.blindauction.blindauctionshopproject.repository.PurchasePermissionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class SellerService {
    private final ProductRepository productRepository;
    private final PurchasePermissionRepository purchasePermissionRepository;

    // 나의 판매상품 등록
    @Transactional
    public void registerProduct(ProductRegisterRequest productRegisterRequest) {
        Product product = new Product(productRegisterRequest.getTitle(), productRegisterRequest.getPrice(), productRegisterRequest.getProductDetail());
        productRepository.save(product);
//        return new SellerProductResponse(product);
    }

    // 나의 전체 판매상품 조회
    @Transactional
    public List<SellerProductResponse> getSellerProductList() {
        List<Product> products = productRepository.findAllByOrderByModifiedAtDesc();
        List<SellerProductResponse> sellerProductResponses = new ArrayList<>();
        for (Product product : products) {
            sellerProductResponses.add(new SellerProductResponse(product));
        }
        return sellerProductResponses;
    }

    // 나의 개별 판매상품 조회
    @Transactional
    public SellerProductDetailResponse getSellerProduct(Long productId) {
        Product product = productRepository.findById(productId).orElseThrow(
                () -> new IllegalArgumentException("id가 존재하지 않습니다.")
        );
        return new SellerProductDetailResponse(product);
    }

    // 나의 판매상품 수정
    @Transactional
    public void updateSellerProduct(Long productId, ProductUpdateRequest productUpdateRequest, User user) {
        Product product = productRepository.findById(productId).orElseThrow(
                () -> new IllegalArgumentException("게시물이 존재하지 않습니다.")
        );

        if (product.getSeller().getId() != user.getId()) {
            throw new IllegalArgumentException("판매자가 아닙니다.");
        }

        product.update(productUpdateRequest.getTitle(), productUpdateRequest.getPrice(), productUpdateRequest.getProductDetail());
        productRepository.save(product);
//        return new SellerProductResponse(product);
    }

    // 나의 판매상품 삭제
    @Transactional
    public void deleteSellerProduct(Long productId, User user) {
        Product product = productRepository.findById(productId).orElseThrow(
                () -> new IllegalArgumentException("게시물이 존재하지 않습니다.")
        );

        if (product.getSeller().getId() != user.getId()) {
            throw new IllegalArgumentException("판매자가 아닙니다.");
        }

        productRepository.delete(product);
    }

    // 전체상품 고객(구매)요청 목록 조회
/*    @Transactional
    public List<ProductPurchasePermissionResponse> getPurchasePermissionList() {
        List<Product> products = productRepository.findAllByOrderByModifiedAtDesc();
        List<ProductPurchasePermissionResponse> productPurchasePermissionResponses = new ArrayList<>(); // ?

        for (Product product : products) {
            List<PurchasePermission> purchasePermissions = purchasePermissionRepository.findPurchasePermissionBy();
            List<PurchasePermissionResponse> purchasePermissionResponses = new ArrayList<>();
            User user;
            for (PurchasePermission purchasePermission : purchasePermissions) {
                purchasePermissionResponses.add(new PurchasePermissionResponse(user,purchasePermission));
            }
            productPurchasePermissionResponses.add(new ProductPurchasePermissionResponse(product));
        }
        return productPurchasePermissionResponses;
    }*/
}