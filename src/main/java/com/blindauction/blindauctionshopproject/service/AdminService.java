package com.blindauction.blindauctionshopproject.service;


import com.blindauction.blindauctionshopproject.dto.admin.*;

import com.blindauction.blindauctionshopproject.dto.security.AdminUsernameAndRoleResponse;
import com.blindauction.blindauctionshopproject.dto.security.UsernameAndRoleResponse;
import com.blindauction.blindauctionshopproject.entity.*;

import com.blindauction.blindauctionshopproject.repository.AdminRepository;

import com.blindauction.blindauctionshopproject.repository.SellerPermissionRepository;
import com.blindauction.blindauctionshopproject.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
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
        AdminRoleEnum role = AdminRoleEnum.ADMIN;

        Admin admin = new Admin(username, nickname, password, role);
        adminRepository.save(admin);
    }

    public List<UserResponse> getUserList(User user) {

        List<User> userList = userRepository.findAllByRole(USER);
        List<UserResponse> userResponseList = new ArrayList<>();

        for(long i = 0L; i < userList.size(); i++) { // 페이징 : 가입한 순서대로 표기되는지 확인해봐야 합니다
            userRepository.findById(i).orElseThrow(
                    () -> new IllegalArgumentException("존재하지 않는 고객입니다.")
            );

            UserResponse userResponse = new UserResponse(user.getUsername(), user.getNickname());
            userResponseList.add(userResponse);
        }

        return userResponseList;
    }

    public List<SellerDetailResponse> getSellerList(User user) { // 인증된 사용자 정보를 파라미터로 받아와야 되는지?

        UserRoleEnum role = SELLER;

        List<User> sellerList = userRepository.findAllByRole(role);
        List<SellerDetailResponse> sellerDetailResponseList = new ArrayList<>();

        for(long i = 0L; i < sellerList.size(); i++) { // 페이징 : 판매자 권한을 획득한 순서대로 표기되는지 확인해봐야 합니다
            User seller = userRepository.findByIdAndRole(i, role).orElseThrow(
                    () -> new IllegalArgumentException("존재하지 않는 판매자입니다.")
            );

            SellerDetailResponse sellerDetailResponse = new SellerDetailResponse(
                    seller.getUsername(),
                    seller.getNickname(),
                    seller.getPhoneNum(),
                    seller.getSellerDetail());
            sellerDetailResponseList.add(sellerDetailResponse);
        }

        return sellerDetailResponseList;
    }

    public List<SellerPermissonResponse> getSellerPermissionList(User user) {

        List<SellerPermission> sellerPermissionList = sellerPermissionRepository.findAllByOrderByIdDesc();
        List<SellerPermissonResponse> sellerPermissionResponseList = new ArrayList<>();

        for(SellerPermission sellerPermission : sellerPermissionList) {
            sellerPermissionResponseList.add(new SellerPermissonResponse(
                    user.getUsername(),
                    user.getNickname(),
                    sellerPermission.getPhoneNum(),
                    sellerPermission.getPermissionDetail()));

        }

        return sellerPermissionResponseList;
    }

    @Transactional
    public void acceptSellerRole(Long userId, User user) {

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
    public void deleteSellerRole(Long userId) {

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
    public AdminUsernameAndRoleResponse loginAdmin(AdminLoginRequest adminLoginRequest) {
        String username = adminLoginRequest.getUsername();
        String password = adminLoginRequest.getPassword();

        Admin admin = adminRepository.findByUsername(username).orElseThrow(
                () -> new IllegalArgumentException("해당 id 는 존재하지 않습니다.")
        );
        if (!passwordEncoder.matches(password, admin.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다");
        }
        return new AdminUsernameAndRoleResponse(username, admin.getRole());
    }
}
