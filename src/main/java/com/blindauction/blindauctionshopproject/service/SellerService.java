package com.blindauction.blindauctionshopproject.service;

import com.blindauction.blindauctionshopproject.dto.seller.*;
import com.blindauction.blindauctionshopproject.entity.*;
import com.blindauction.blindauctionshopproject.repository.ProductRepository;
import com.blindauction.blindauctionshopproject.repository.UserRepository;
import com.blindauction.blindauctionshopproject.repository.PurchasePermissionRepository;

import com.blindauction.blindauctionshopproject.util.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
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

    // 나의 판매상품 등록 [확인ㅇ] // 권한 상관없이 등록됨
    @Transactional
    public void registerProduct(ProductRegisterRequest productRegisterRequest, String username) {
        User user = userRepository.findByUsername(username).orElseThrow(
                () -> new IllegalArgumentException("해당 유저는 존재하지 않습니다")
        );
        String title = productRegisterRequest.getTitle();
        Long price = productRegisterRequest.getPrice();
        String productDetail = productRegisterRequest.getProductDetail();
        if (user.isSeller()) {
            Product product = new Product(user, title, price, productDetail);
            productRepository.save(product);
        } else throw new IllegalArgumentException("판매자 권한 유저만 판매글을 작성할 수 있습니다.");
    }

    // 나의 전체 판매상품 조회 // 페이징 필요 // 권한 상관없이 조회됨
    @Transactional
    public List<SellerProductResponse> getSellerProductList(UserDetailsImpl userDetails) {
        User user = userDetails.getUser();
        if (!user.isSeller()) {
            throw new IllegalArgumentException("판매자만 조회할 수 있습니다.");
        }
        List<Product> products = productRepository.findAllBySeller(user);
        List<SellerProductResponse> sellerProductResponses = new ArrayList<>();
        for (Product product : products) {
            List<PurchasePermission> purchasePermissions = purchasePermissionRepository.findPurchasePermissionByProduct(product);
            List<PurchasePermissionResponse> purchasePermissionResponses = new ArrayList<>();
            for (PurchasePermission purchasePermission : purchasePermissions) {
                purchasePermissionResponses.add(new PurchasePermissionResponse(purchasePermission.getBidder().getUsername(), purchasePermission.getBidder().getNickname(),purchasePermission.getMsg(), purchasePermission.getPrice()));
            }
            int bidderCnt = purchasePermissionResponses.size();
            sellerProductResponses.add(new SellerProductResponse(product.getId(), product.getTitle(), product.getPrice(), product.getProductDetail(), bidderCnt));
        }
        return sellerProductResponses;
    }

    // 나의 개별 판매상품 조회  // bidderList에서 username이 없음 // 권한 상관없이 조회됨
    @Transactional
    public List<SellerProductDetailResponse> getSellerProduct(Long productId) {
        List<Product> products = productRepository.findProductById(productId);
        List<SellerProductDetailResponse> sellerProductDetailResponses = new ArrayList<>();

        for (Product product1 : products) {
            List<PurchasePermission> purchasePermissions = purchasePermissionRepository.findPurchasePermissionByProduct(product1);
            List<PurchasePermissionResponse> purchasePermissionResponses = new ArrayList<>();

            for (PurchasePermission purchasePermission : purchasePermissions) {
                purchasePermissionResponses.add(new PurchasePermissionResponse(purchasePermission.getBidder().getUsername(), purchasePermission.getBidder().getNickname(), purchasePermission.getMsg(), purchasePermission.getPrice()));
            }

            sellerProductDetailResponses.add(new SellerProductDetailResponse(product1.getTitle(), product1.getPrice(), product1.getProductDetail(), purchasePermissionResponses));
        }

        return sellerProductDetailResponses;
    }

    // 나의 판매상품 수정 [확인ㅇ] // 권한 상관없이 수정됨
    @Transactional
    public void updateSellerProduct(Long productId, ProductUpdateRequest productUpdateRequest, String username) {
        User user = userRepository.findByUsername(username).orElseThrow(
                () -> new IllegalArgumentException("해당 유저는 존재하지 않습니다")
        );

        Product product = productRepository.findById(productId).orElseThrow(
                () -> new IllegalArgumentException("게시물이 존재하지 않습니다.")
        );

        if (user.isSeller()) {
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

        if (product.checkUsernameIsProductSeller(username)) {
            productRepository.delete(product);
        } else throw new IllegalArgumentException("판매자 권한 유저만 판매글을 삭제할 수 있습니다.");

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

    // 전체상품 고객(구매)요청 목록 조회  // 페이징 필요  /
    @Transactional
    public ProductPurchasePermissionResponse getPurchasePermissionList(String username, int page) {
        //해당 유저의
        List<Product> products =  productRepository.findAllByOrderByModifiedAt();
        List<ProductPurchasePermissionResponse> productPurchasePermissionResponses = new ArrayList<>();
//
        for (Product product : products) {
            List<PurchasePermission> purchasePermissions = purchasePermissionRepository.findPurchasePermissionByProduct(product);
            List<PurchasePermissionResponse> purchasePermissionResponses = new ArrayList<>();
            for (PurchasePermission purchasePermission : purchasePermissions) {
                purchasePermissionResponses.add(new PurchasePermissionResponse(purchasePermission.getBidder().getUsername(), purchasePermission.getBidder().getNickname(),purchasePermission.getMsg(), purchasePermission.getPrice()));
            }
            productPurchasePermissionResponses.add(new ProductPurchasePermissionResponse(product.getId(), product.getTitle(), product.getPrice(), purchasePermissionResponses));
        }
        return productPurchasePermissionResponses;
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
        //3. purchasePermission 이 WATTING 상태면 ACCEPTANCE 나 REFUSAL로 변경
        TransactionStatusEnum status = purchasePermissionUpdateRequest.getTransactionStatus();
        if (purchasePermission.checkStatusIsWaiting()) {
            purchasePermission.updateStatus(status);
        } else throw new IllegalArgumentException("이미 처리 완료된 거래요청입니다");
        //4. WATTING 이 아닌경우 이미 처리 완료된 거래신청입니다.
    }

}
