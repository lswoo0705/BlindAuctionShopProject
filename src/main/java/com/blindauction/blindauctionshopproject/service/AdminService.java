package com.blindauction.blindauctionshopproject.service;


import com.blindauction.blindauctionshopproject.dto.admin.*;

import com.blindauction.blindauctionshopproject.dto.security.UsernameAndRoleResponse;
import com.blindauction.blindauctionshopproject.dto.admin.SellerDetailResponse;
import com.blindauction.blindauctionshopproject.dto.admin.SellerPermissionResponse;
import com.blindauction.blindauctionshopproject.dto.admin.UserResponse;
import com.blindauction.blindauctionshopproject.entity.*;

import com.blindauction.blindauctionshopproject.repository.AdminRepository;

import com.blindauction.blindauctionshopproject.repository.SellerPermissionRepository;
import com.blindauction.blindauctionshopproject.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static com.blindauction.blindauctionshopproject.entity.UserRoleEnum.SELLER;
import static com.blindauction.blindauctionshopproject.entity.UserRoleEnum.USER;

@Service
@RequiredArgsConstructor
public class AdminService {
    private UserRepository userRepository;
    private final AdminRepository adminRepository;

    private SellerPermissionRepository sellerPermissionRepository;
    private final PasswordEncoder passwordEncoder;
    private static final String ADMIN_TOKEN = "eyJzdWIiOiJoZWxsb3dvcmxkIiwibm";

    @Transactional
    public void signupAdmin(AdminSignupRequest adminSignupRequest) {
        String username = adminSignupRequest.getUsername();
        String nickname = adminSignupRequest.getNickname();
        String password = passwordEncoder.encode(adminSignupRequest.getPassword());

        Optional<Admin> found = adminRepository.findByUsername(username);
        if (found.isPresent()) {
            throw new IllegalArgumentException("중복된 관리자 아이디가 존재합니다.");
        }
        if (!adminSignupRequest.getAdminToken().equals(ADMIN_TOKEN)) {
            throw new IllegalArgumentException("관리자 토큰이 일치하지 않습니다");
        }
        UserRoleEnum role = UserRoleEnum.ADMIN;

        Admin admin = new Admin(username, nickname, password, role);
        adminRepository.save(admin);
    }

    @Transactional // 회원목록 조회
    public Page<UserResponse> getUserList() {
        return userRepository.findAllByRole(USER, PageRequest.of(10, 10)).map(UserResponse::new);
        // (클래스 :: 메서드)
    }

    @Transactional
    public Page<SellerDetailResponse> getSellerList() { // 판매자목록 조회
        return userRepository.findAllByRole(SELLER, PageRequest.of(10, 10))
                .map(user -> new SellerDetailResponse(user.getUsername(), user.getNickname(), user.getPhoneNum(), user.getSellerDetail()));
    }

    @Transactional
    public Page<SellerPermissionResponse> getSellerPermissionList() { // 판매자권한 요청목록 조회
        return sellerPermissionRepository.findAllByOrderByModifiedAtDesc(PageRequest.of(10, 10)).map(SellerPermissionResponse::new);
    }

    @Transactional
    public void acceptSellerRole(Long userId, User user) { // 판매자 권한 승인

        SellerPermission sellerPermission = sellerPermissionRepository.findById(userId).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 요청입니다.")
        );

        user = userRepository.findByIdAndRole(userId, USER).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 고객이거나 권한이 고객이 아닙니다.")
        );

       userRepository.save(
               new User(
                       user.getUsername(),
                       user.getNickname(),
                       user.getPassword(),
                       sellerPermission.getPhoneNum(),
                       SELLER));
    }

    @Transactional
    public void deleteSellerRole(Long userId) { // 판매자 권한 삭제

        User seller = userRepository.findByIdAndRole(userId, SELLER).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 고객이거나 권한이 판매자가 아닙니다.")
        );

        userRepository.save(
                new User(
                        seller.getUsername(),
                        seller.getNickname(),
                        seller.getPassword(),
                        USER));
    }

    //관리자 로그인
    public UsernameAndRoleResponse loginAdmin(AdminLoginRequest adminLoginRequest) {
        String username = adminLoginRequest.getUsername();
        String password = adminLoginRequest.getPassword();

        Admin admin = adminRepository.findByUsername(username).orElseThrow(
                () -> new IllegalArgumentException("해당 id 는 존재하지 않습니다.")
        );
        if (!passwordEncoder.matches(password, admin.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다");
        }
        return new UsernameAndRoleResponse(username, admin.getRole());
    }
}
