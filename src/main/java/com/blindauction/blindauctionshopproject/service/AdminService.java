package com.blindauction.blindauctionshopproject.service;


import com.blindauction.blindauctionshopproject.dto.admin.*;

import com.blindauction.blindauctionshopproject.dto.security.UsernameAndRoleResponse;
import com.blindauction.blindauctionshopproject.dto.admin.SellerDetailResponse;
import com.blindauction.blindauctionshopproject.dto.admin.SellerPermissionResponse;
import com.blindauction.blindauctionshopproject.dto.admin.UserResponse;
import com.blindauction.blindauctionshopproject.entity.*;

import com.blindauction.blindauctionshopproject.repository.SellerPermissionRepository;
import com.blindauction.blindauctionshopproject.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.blindauction.blindauctionshopproject.entity.UserRoleEnum.SELLER;
import static com.blindauction.blindauctionshopproject.entity.UserRoleEnum.USER;

@Service
@RequiredArgsConstructor
public class AdminService {
    private final UserRepository userRepository;
    private final SellerPermissionRepository sellerPermissionRepository;
    private final PasswordEncoder passwordEncoder;
    private static final String ADMIN_TOKEN = "eyJzdWIiOiJoZWxsb3dvcmxkIiwibm";

    @Transactional
    public void signupAdmin(AdminSignupRequest adminSignupRequest) {
        String username = adminSignupRequest.getUsername();
        String nickname = adminSignupRequest.getNickname();
        String password = passwordEncoder.encode(adminSignupRequest.getPassword());

        Optional<User> found = userRepository.findByUsername(username);
        if (found.isPresent()) {
            throw new IllegalArgumentException("중복된 관리자 아이디가 존재합니다.");
        }
        if (!adminSignupRequest.getAdminToken().equals(ADMIN_TOKEN)) {
            throw new IllegalArgumentException("관리자 토큰이 일치하지 않습니다");
        }
        UserRoleEnum role = UserRoleEnum.ADMIN;

        User user = new User(username, nickname, password, role);
        userRepository.save(user);
    }

    @Transactional // 회원목록 조회
    public List<UserResponse> getUserList() {
        return userRepository.findAllByRole(USER, PageRequest.of(10, 10)).map(UserResponse::new).stream().toList();
        // (클래스 :: 메서드)
    }

    @Transactional
    public Page<SellerDetailResponse> getSellerList() { // 판매자목록 조회
        return userRepository.findAllByRole(SELLER, PageRequest.of(10, 10))
                .map(user -> new SellerDetailResponse(user.getUsername(), user.getNickname(), user.getNickname(), user.getSellerDetail()));
    }

    @Transactional
    public Page<SellerPermissionResponse> getSellerPermissionList() { // 판매자권한 요청목록 조회
        return sellerPermissionRepository.findAllByOrderByModifiedAtDesc(PageRequest.of(10, 10)).map(SellerPermissionResponse::new);
    }

    @Transactional
    public void acceptSellerRole(Long userId) { // 판매자 권한 승인
        //1. userid 에 해당하는 유저가 존재하는지
        User user = userRepository.findById(userId).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 UserId 입니다.")
        );
        //2. 존재한다면 그 유저의 역할을 확인. user의 권한이 USER인 경우, SellerPermission Repository에서 그 유저가 작성한 permisssion이 있는지 확인
        if (user.isUser()) {
            SellerPermission sellerPermission = sellerPermissionRepository.findByUser(user).orElseThrow(
                    () -> new IllegalArgumentException("해당 유저는 판매자 전환신청서를 작성하지 않았습니다.")
            );
            //3. permisssion이 존재하면 그 permisssion에서 전화번호를 가져와서, 권한 USER >> SELLER / 전화번호를 등록
            String phoneNum = sellerPermission.getPhoneNum();
            user.updateUserToSeller();
            user.updateUserPhoneNum(phoneNum);

        } else throw new IllegalArgumentException("일반유저가 아닙니다.");
    }

    @Transactional
    public void deleteSellerRole(Long userId) { // 판매자 권한 삭제
        User seller = userRepository.findById(userId).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 userid 입니다")
        );
        if(!seller.isSeller()){
            throw new IllegalArgumentException("해당 유저는 SELLER가 아닙니다");
        }
        seller.updateSellerToUser();


    }

    //관리자 로그인
    public UsernameAndRoleResponse loginAdmin(AdminLoginRequest adminLoginRequest) {
        String username = adminLoginRequest.getUsername();
        String password = adminLoginRequest.getPassword();

        User user = userRepository.findByUsername(username).orElseThrow(
                () -> new IllegalArgumentException("해당 id 는 존재하지 않습니다.")
        );
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다");
        }
        return new UsernameAndRoleResponse(user.getUsername(), user.getRole());
    }
}
