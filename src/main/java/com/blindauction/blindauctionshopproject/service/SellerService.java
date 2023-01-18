package com.blindauction.blindauctionshopproject.service;

import com.blindauction.blindauctionshopproject.dto.seller.ProductRegisterRequest;
import com.blindauction.blindauctionshopproject.dto.seller.SellerProductResponse;
import com.blindauction.blindauctionshopproject.entity.Product;
import com.blindauction.blindauctionshopproject.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class SellerService {
    private final ProductRepository productRepository;

    @Transactional
    public SellerProductResponse registerProduct(ProductRegisterRequest productRegisterRequest) {  // void-> SellerResponse? 암튼 바꿔야함
        Product product = new Product(productRegisterRequest.getTitle(), productRegisterRequest.getPrice(), productRegisterRequest.getProductDetail());
        productRepository.save(product);
        return new SellerProductResponse(product);
    }
}