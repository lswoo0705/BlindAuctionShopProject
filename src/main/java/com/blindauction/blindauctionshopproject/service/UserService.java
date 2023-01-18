package com.blindauction.blindauctionshopproject.service;

import com.blindauction.blindauctionshopproject.dto.user.UserProfileResponse;
import com.blindauction.blindauctionshopproject.entity.SellerPermission;
import com.blindauction.blindauctionshopproject.repository.SellerPermissionRepository;
import com.blindauction.blindauctionshopproject.dto.user.UserSignupRequest;
import com.blindauction.blindauctionshopproject.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final SellerPermissionRepository sellerPermissionRepository;
    private final UserRepository userRepository;

    // 나의 프로필 조회
    public UserProfileResponse getUserProfile() {

    }

    // 판매자 등록 요청
    public ResponseEntity registerSellerPermission(String phoneNum, String permissionDetail) {
        SellerPermission sellerPermission = new SellerPermission(phoneNum, permissionDetail);
        sellerPermissionRepository.save(sellerPermission);
        return
    }
*/

    //유저 회원가입

}
