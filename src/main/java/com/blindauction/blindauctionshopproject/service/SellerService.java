package com.blindauction.blindauctionshopproject.service;

import com.blindauction.blindauctionshopproject.dto.seller.*;
import com.blindauction.blindauctionshopproject.entity.*;
import com.blindauction.blindauctionshopproject.repository.ProductRepository;
import com.blindauction.blindauctionshopproject.repository.UserRepository;
import com.blindauction.blindauctionshopproject.repository.PurchasePermissionRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.*;
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
    public void registerProduct(ProductRegisterRequest productRegisterRequest, String username) {
        User user = userRepository.findByUsername(username).orElseThrow(
                () -> new IllegalArgumentException("해당 유저는 존재하지 않습니다")
        );
        String title = productRegisterRequest.getTitle();
        Long price = productRegisterRequest.getPrice();
        String productDetail = productRegisterRequest.getProductDetail();

        Product product = new Product(user, title, price, productDetail);
        productRepository.save(product);
    }

    // 나의 전체 판매상품 조회
    @Transactional
    public Page<SellerProductResponse> getSellerProductList(String username, int page) {
        User user = userRepository.findByUsername(username).orElseThrow(
                () -> new IllegalArgumentException("해당 유저는 존재하지 않습니다")
        );

        PageRequest pageable = PageRequest.of(page, 5, Sort.by(Sort.Direction.ASC, "id"));
        Page<Product> products = productRepository.findAllBySeller(user, pageable);
        return products.map(SellerProductResponse::new);
    }

    // 나의 개별 판매상품 조회
    @Transactional
    public List<SellerProductDetailResponse> getSellerProduct(String username, Long productId) {
        userRepository.findByUsername(username).orElseThrow(
                () -> new IllegalArgumentException("해당 유저는 존재하지 않습니다")
        );
        List<Product> products = productRepository.findProductById(productId);
        if (products.isEmpty()) {
            throw new IllegalArgumentException("해당 게시글이 없습니다");
        }
        List<PurchasePermission> purchasePermissions = products.stream().map(purchasePermissionRepository::findByProduct).toList();
        List<PurchasePermissionResponse> purchasePermissionResponses = new ArrayList<>();
        for (PurchasePermission purchasePermission : purchasePermissions) {

            if (purchasePermission == null) {
                return products.stream().map(SellerProductDetailResponse::new).toList();
            }
            purchasePermissionResponses.add(new PurchasePermissionResponse(purchasePermission));

        }
        return products.stream().map(product -> new SellerProductDetailResponse(product, purchasePermissionResponses)).toList();
    }

    // 나의 판매상품 수정
    @Transactional
    public void updateSellerProduct(Long productId, ProductUpdateRequest productUpdateRequest, String username) {
        Product product = productRepository.findById(productId).orElseThrow(
                () -> new IllegalArgumentException("게시물이 존재하지 않습니다.")
        );

        if (product.checkUsernameIsProductSeller(username)) {
            product.update(productUpdateRequest.getTitle(), productUpdateRequest.getPrice(), productUpdateRequest.getProductDetail());
        } else throw new IllegalArgumentException("자신이 작성한 판매글만 수정할 수 있습니다.");
    }


    // 나의 판매상품 삭제
    @Transactional
    public void deleteSellerProduct(Long productId, String username, ProductDeleteRequest productDeleteRequest) {
        User user = userRepository.findByUsername(username).orElseThrow(
                () -> new IllegalArgumentException("해당 유저는 존재하지 않습니다")
        );

        Product product = productRepository.findById(productId).orElseThrow(
                () -> new IllegalArgumentException("게시물이 존재하지 않습니다.")
        );

        if (!product.checkUsernameIsProductSeller(user.getUsername())) {
            throw new IllegalArgumentException("자신이 작성한 판매글만 삭제할 수 있습니다.");
        }

        if (!passwordEncoder.matches(productDeleteRequest.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }
        productRepository.deleteById(productId);
    }

    //나의 판매자 프로필 조회
    @Transactional
    public SellerProfileResponse getSellerProfile(String username) {
        //1. username 에 해당하는 유저가 있는지 찾는다.
        User user = userRepository.findByUsername(username).orElseThrow(
                () -> new IllegalArgumentException("해당 username 의 유저가 존재하지 않습니다")
        );
        //3. 해당 유저가 셀러인지 확인한다
        if (!user.isSeller()) {
            throw new IllegalArgumentException("판매자인 유저만 사용 가능한 기능입니다.");
        }
        //4. user 객체의 정보 변경 (닉네임, 셀러디테일)
        return new SellerProfileResponse(user.getUsername(), user.getNickname(), user.getSellerDetail());
    }

    //나의 판매자 프로필 수정
    @Transactional
    public void updateSellerProfile(SellerProfileUpdateRequest sellerProfileUpdateRequest, String username) {
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

    // 전체상품 고객(구매)요청 목록 조회
    @Transactional
    public Page<ProductPurchasePermissionResponse> getPurchasePermissionList(String username, int page) {
        User user = userRepository.findByUsername(username).orElseThrow(
                () -> new IllegalArgumentException("해당 username 의 유저가 존재하지 않습니다")
        );

        PageRequest pageable = PageRequest.of(page, 5, Sort.by(Sort.Direction.ASC, "ModifiedAt"));
        Page<Product> products = productRepository.findAllBySellerUsername(username, pageable);
        List<PurchasePermission> purchasePermissions = products.stream().map(product -> purchasePermissionRepository.findByProduct(product)).toList();
        List<PurchasePermissionResponse> purchasePermissionResponses = new ArrayList<>();
        for (PurchasePermission purchasePermission : purchasePermissions) {
            purchasePermissionResponses.add(new PurchasePermissionResponse(purchasePermission));
        }
        return products.map(product -> new ProductPurchasePermissionResponse(product, purchasePermissionResponses));
    }

    // 고객(거래)요청 수락&완료
    @Transactional
    public void updatePurchasePermission(Long permissionId, PurchasePermissionUpdateRequest purchasePermissionUpdateRequest, String username) {
        //1. 해당 permisssionId 의 판매요청글이 있는지 확인하고 불러옴
        PurchasePermission purchasePermission = purchasePermissionRepository.findById(permissionId).orElseThrow(
                () -> new IllegalArgumentException("해당 permissionId의 판매요청글이 존재하지 않습니다")
        );
        //2. username 이 해당 product 글의 작성자인지 확인
        if (!purchasePermission.getProduct().checkUsernameIsProductSeller(username)) {
            throw new IllegalArgumentException("자신이 작상한 판매글에 달린 거래요청만 관리할 수 있습니다.");
        }
        //3. purchasePermission 이 WAITING 상태면 ACCEPTANCE 나 REFUSAL로 변경
        PermissionStatusEnum status = purchasePermissionUpdateRequest.getTransactionStatus();
        if (purchasePermission.checkStatusIsWaiting()) {
            purchasePermission.updateStatus(status);
        } else throw new IllegalArgumentException("이미 처리 완료된 거래요청입니다");
        //4. WAITING 이 아닌경우 이미 처리 완료된 거래신청입니다.
    }

}
