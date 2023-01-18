package com.blindauction.blindauctionshopproject.service;

import com.blindauction.blindauctionshopproject.dto.user.UserProfileResponse;
import com.blindauction.blindauctionshopproject.entity.SellerPermission;
import com.blindauction.blindauctionshopproject.entity.User;
import com.blindauction.blindauctionshopproject.entity.UserRoleEnum;
import com.blindauction.blindauctionshopproject.repository.SellerPermissionRepository;
import com.blindauction.blindauctionshopproject.dto.user.UserSignupRequest;
import com.blindauction.blindauctionshopproject.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final SellerPermissionRepository sellerPermissionRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    // 나의 프로필 조회
    public UserProfileResponse getUserProfile() {

    }

    // 판매자 등록 요청
    public ResponseEntity registerSellerPermission(String phoneNum, String permissionDetail) {
        SellerPermission sellerPermission = new SellerPermission(phoneNum, permissionDetail);
        sellerPermissionRepository.save(sellerPermission);
        return
    }
    //유저 회원가입
    public void signupUser(UserSignupRequest userSignupRequest) {
        String username = userSignupRequest.getUsername();
        String nickname = userSignupRequest.getNickname();
        String password = passwordEncoder.encode(userSignupRequest.getPassword());

        Optional<User> foundUsername = userRepository.findByUsername(username);
        if(foundUsername.isPresent()){
            throw new IllegalArgumentException("중복된 아이디가 존재합니다.");
        }
        Optional<User> foundNickname = userRepository.findByNickname(nickname);
        if(foundNickname.isPresent()){
            throw new IllegalArgumentException("중복된 닉네임이 존재합니다.");
        }
        UserRoleEnum role = UserRoleEnum.USER;

        User user = new User(username, nickname, password, role);
        userRepository.save(user);
    }

}
