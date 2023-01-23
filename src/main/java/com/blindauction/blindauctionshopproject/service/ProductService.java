package com.blindauction.blindauctionshopproject.service;

import com.blindauction.blindauctionshopproject.dto.product.ProductDetailResponse;
import com.blindauction.blindauctionshopproject.dto.product.ProductResponse;
import com.blindauction.blindauctionshopproject.entity.Product;
import com.blindauction.blindauctionshopproject.entity.PurchasePermission;
import com.blindauction.blindauctionshopproject.entity.PermissionStatusEnum;
import com.blindauction.blindauctionshopproject.entity.User;
import com.blindauction.blindauctionshopproject.repository.ProductRepository;
import com.blindauction.blindauctionshopproject.repository.PurchasePermissionRepository;
import com.blindauction.blindauctionshopproject.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
        Pageable pageable = PageRequest.of(page, 5, Sort.by(Sort.Direction.ASC, "id"));
        Page<Product> products = productRepository.findAll(pageable);
        return products.map(ProductResponse::new);
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

        if (product.checkUsernameIsProductSeller(username)) {
            throw new IllegalArgumentException("판매글 작성 당사자는 구매요청을 할 수 없습니다.");
        }

        if (!purchasePermissionRepository.findByProductAndBidder(product, user).isEmpty()) {
            throw new IllegalArgumentException("이미 구매요청하신 상품입니다.");
        }

        PermissionStatusEnum statusEnum = PermissionStatusEnum.WAITING;

        PurchasePermission purchasePermission = new PurchasePermission(product, user, msg, price, statusEnum);
        product.plusBidderCnt();
        purchasePermissionRepository.save(purchasePermission);
    }
}
