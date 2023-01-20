package com.blindauction.blindauctionshopproject.service;

import com.blindauction.blindauctionshopproject.dto.product.ProductDetailResponse;
import com.blindauction.blindauctionshopproject.dto.product.ProductResponse;
import com.blindauction.blindauctionshopproject.entity.Product;
import com.blindauction.blindauctionshopproject.entity.PurchasePermission;
import com.blindauction.blindauctionshopproject.entity.TransactionStatusEnum;
import com.blindauction.blindauctionshopproject.entity.User;
import com.blindauction.blindauctionshopproject.repository.ProductRepository;
import com.blindauction.blindauctionshopproject.repository.PurchasePermissionRepository;
import com.blindauction.blindauctionshopproject.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final PurchasePermissionRepository purchasePermissionRepository;

    // 전체 판매상품 목록 조회
    @Transactional
    public Page<ProductResponse> getProductList(int page) {
        Pageable pageable = PageRequest.of(page, 10);
        Page<Product> products = productRepository.findAllByOrderByModifiedAtDesc(pageable);
        return products.map(ProductResponse::new);

        // 리스트 스트림으로 할 때
//        return products.stream().map(ProductResponse::new).collect(Collectors.toList());
//                                 내보내려는 형식                리스트로 만들어주겠다
    }

    // 개별 판매상품 상세 조회
    public ProductDetailResponse getProductById(Long productId) {
        Product product = productRepository.findById(productId).orElseThrow(
                () -> new IllegalArgumentException("해당하는 Id가 존재하지 않습니다.")
        );
        return new ProductDetailResponse(product);
    }

    // 판매자에게 구입 요청
    @Transactional
    public void sendPurchasePermission(String msg, Long price, String username, Long productId) {
        Product product = productRepository.findById(productId).orElseThrow(
                () -> new IllegalArgumentException("해당 상품을 찾을 수 없습니다.")
        );

        User user = userRepository.findByUsername(username).orElseThrow(
                () -> new IllegalArgumentException("해당 유저를 찾을 수 없습니다.")
        );

        TransactionStatusEnum statusEnum = TransactionStatusEnum.WAITING;

        PurchasePermission purchasePermission = new PurchasePermission(product, user, msg, price, statusEnum);
        purchasePermissionRepository.save(purchasePermission);
    }
}
