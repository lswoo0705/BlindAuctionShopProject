package com.blindauction.blindauctionshopproject.service;

import com.blindauction.blindauctionshopproject.dto.user.UserProfileResponse;
import com.blindauction.blindauctionshopproject.entity.SellerPermission;
import com.blindauction.blindauctionshopproject.entity.User;
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
    public UserProfileResponse getUserProfile(String userInfo) {
        User user = userRepository.findByUsername(userInfo).orElseThrow(
                () -> new IllegalArgumentException("해당 유저를 찾을 수 없습니다.")
        );
        String userProfileName = user.getUsername();
        String userProfileNick = user.getNickname();
        return new UserProfileResponse(userProfileName, userProfileNick);
    }

    // 판매자 등록 요청
    public void registerSellerPermission(String phoneNum, String permissionDetail) {
        SellerPermission sellerPermission = new SellerPermission(phoneNum, permissionDetail);
        sellerPermissionRepository.save(sellerPermission);
    }

    //유저 회원가입

}
