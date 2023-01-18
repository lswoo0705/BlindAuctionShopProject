package com.blindauction.blindauctionshopproject.service;

import com.blindauction.blindauctionshopproject.dto.seller.ProductRegisterRequest;
import com.blindauction.blindauctionshopproject.dto.seller.SellerProductDetailResponse;
import com.blindauction.blindauctionshopproject.dto.seller.SellerProductResponse;
import com.blindauction.blindauctionshopproject.entity.Product;
import com.blindauction.blindauctionshopproject.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class SellerService {
    private final ProductRepository productRepository;

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
}