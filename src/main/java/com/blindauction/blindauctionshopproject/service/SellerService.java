package com.blindauction.blindauctionshopproject.service;

import com.blindauction.blindauctionshopproject.dto.seller.*;
import com.blindauction.blindauctionshopproject.entity.*;
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
    // productId, title, price, productDetail, bidderCnt
    @Transactional
    public void registerProduct(ProductRegisterRequest productRegisterRequest, String username) {
        User user = userRepository.findByUsername(username).orElseThrow(
                () -> new IllegalArgumentException("해당 유저는 존재하지 않습니다")
        );
        String title = productRegisterRequest.getTitle();
        Long price = productRegisterRequest.getPrice();
        String productDetail = productRegisterRequest.getProductDetail();
        if (user.isSeller()){
            Product product = new Product(user, title, price, productDetail);
            productRepository.save(product);
        } else throw new IllegalArgumentException("판매자 권한 유저만 판매글을 작성할 수 있습니다.");
    }

    // 나의 전체 판매상품 조회  // 페이징 필요
    @Transactional
    public List<SellerProductResponse> getSellerProductList() {
        List<Product> products = productRepository.findAllByOrderByModifiedAtDesc();
        List<SellerProductResponse> sellerProductResponses = new ArrayList<>();
        for (Product product : products) {
            sellerProductResponses.add(new SellerProductResponse(product));
        }
        return sellerProductResponses;
    }

    // 나의 개별 판매상품 조회  // bidderList 구현 안 됨
    // title, price, productDetail, bidderList(username, nickname, msg, price)
    @Transactional
    public SellerProductDetailResponse getSellerProduct(Long productId) {
        Product product = productRepository.findById(productId).orElseThrow(
                () -> new IllegalArgumentException("판매글이 존재하지 않습니다.")
        );

//        List<Product> products = productRepository.findAllByOrderByModifiedAtDesc();
//        List<ProductPurchasePermissionResponse> productPurchasePermissionResponses = new ArrayList<>();
//
//        for (Product product : products) {
//            List<PurchasePermission> purchasePermissions = purchasePermissionRepository.findPurchasePermissionBy();
//            List<PurchasePermissionResponse> purchasePermissionResponses = new ArrayList<>();
//            User user = new User();  // ??
//            for (PurchasePermission purchasePermission : purchasePermissions) {
//                purchasePermissionResponses.add(new PurchasePermissionResponse(user,purchasePermission));
//            }
//            productPurchasePermissionResponses.add(new ProductPurchasePermissionResponse(product));
//        }
//        return productPurchasePermissionResponses;

        return new SellerProductDetailResponse(product);
    }

    // 나의 판매상품 수정
    @Transactional
    public void updateSellerProduct(Long productId, ProductUpdateRequest productUpdateRequest, String username) {
        User user = userRepository.findByUsername(username).orElseThrow(
                () -> new IllegalArgumentException("해당 유저는 존재하지 않습니다")
        );

        Product product = productRepository.findById(productId).orElseThrow(
                () -> new IllegalArgumentException("게시물이 존재하지 않습니다.")
        );

        if (user.isSeller()){
            product.update(user, productUpdateRequest.getTitle(), productUpdateRequest.getPrice(), productUpdateRequest.getProductDetail());
        } else throw new IllegalArgumentException("판매자 권한 유저만 판매글을 수정할 수 있습니다.");
    }

    // 나의 판매상품 삭제
    @Transactional
    public void deleteSellerProduct(Long productId, String username) {
        User user = userRepository.findByUsername(username).orElseThrow(
                () -> new IllegalArgumentException("해당 유저는 존재하지 않습니다")
        );

        Product product = productRepository.findById(productId).orElseThrow(
                () -> new IllegalArgumentException("게시물이 존재하지 않습니다.")
        );

        if (user.isSeller()) {
            productRepository.delete(product);
        } else throw new IllegalArgumentException("판매자 권한 유저만 판매글을 삭제할 수 있습니다.");

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

    // 전체상품 고객(구매)요청 목록 조회  // 페이징 필요
    @Transactional
    public List<ProductPurchasePermissionResponse> getPurchasePermissionList() {
        List<Product> products = productRepository.findAllByOrderByModifiedAtDesc();
        List<ProductPurchasePermissionResponse> productPurchasePermissionResponses = new ArrayList<>();

        for (Product product : products) {
            List<PurchasePermission> purchasePermissions = purchasePermissionRepository.findPurchasePermissionBy();
            List<PurchasePermissionResponse> purchasePermissionResponses = new ArrayList<>();
            User user = new User();  // ??
            for (PurchasePermission purchasePermission : purchasePermissions) {
                purchasePermissionResponses.add(new PurchasePermissionResponse(user,purchasePermission));
            }
            productPurchasePermissionResponses.add(new ProductPurchasePermissionResponse(product));
        }
        return productPurchasePermissionResponses;
    }

    // 고객(거래)요청 수락&완료  // 작업중
    @Transactional
    public void updatePurchasePermission(Long permissionId, PurchasePermissionUpdateRequest purchasePermissionUpdateRequest, String username) {
        PurchasePermission purchasePermission = purchasePermissionRepository.findById(permissionId).orElseThrow(
                () -> new IllegalArgumentException("구매 요청이 존재하지 않습니다.")
        );
        //판매자 확인
        if (!purchasePermission.getProduct().getSeller().getUsername().equals(username)) {
            throw new IllegalArgumentException("상품의 판매자만 수정할 수 있습니다.");
        }
        // 대기상태인지 먼저 확인
        if (!purchasePermission.getTransactionStatus().equals(TransactionStatusEnum.WAITING)) { // 수락 or 거부 //수락일경우 예외처리
            throw new IllegalArgumentException("이미 처리된 거래 요청입니다.");
        }
        // 대기인 경우 수락이나 거부를 넣을 수 잇음 <- controller 에서
        // if (조건) { purchasePermission.update(purchasePermissionUpdateRequest.getTransactionStatus()); }  // REFUSAL
        // 그후에 수락
        // 수락
//        purchasePermission.update(purchasePermissionUpdateRequest.getTransactionStatus());  // ACCEPTANCE
    }
}