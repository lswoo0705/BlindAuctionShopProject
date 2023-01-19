package com.blindauction.blindauctionshopproject.service;

import com.blindauction.blindauctionshopproject.dto.seller.*;
import com.blindauction.blindauctionshopproject.entity.Product;
import com.blindauction.blindauctionshopproject.entity.User;
import com.blindauction.blindauctionshopproject.repository.ProductRepository;
import com.blindauction.blindauctionshopproject.repository.UserRepository;
import com.blindauction.blindauctionshopproject.repository.PurchasePermissionRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class SellerService {
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    private final PurchasePermissionRepository purchasePermissionRepository;
    private final PasswordEncoder passwordEncoder;

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

    //나의 판매자 프로필 설정
    @Transactional
    public void getSellerProfile(SellerProfileUpdateRequest sellerProfileUpdateRequest, String username) {
        //1. username 에 해당하는 유저가 있는지 찾는다.
        User user = userRepository.findByUsername(username).orElseThrow(
                () -> new IllegalArgumentException("해당 username 의 유저가 존재하지 않습니다")
        );
        //2. 비밀번호가 일치하는지 확인
        if (!passwordEncoder.matches(sellerProfileUpdateRequest.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }
        //3. 해당 유저가 셀러인지 확인한다
        if (!user.isSeller()) {
            throw new IllegalArgumentException("판매자인 유저만 사용 가능한 기능입니다.");
        }
        //4. user 객체의 정보 변경 (닉네임, 셀러디테일)
        user.updateSellerProfile(sellerProfileUpdateRequest.getNickname(), sellerProfileUpdateRequest.getSellerDetail());
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