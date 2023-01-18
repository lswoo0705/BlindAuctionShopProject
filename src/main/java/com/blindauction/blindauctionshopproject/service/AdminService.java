package com.blindauction.blindauctionshopproject.service;


import com.blindauction.blindauctionshopproject.dto.admin.AdminSignupRequest;

import com.blindauction.blindauctionshopproject.dto.admin.UserResponse;
import com.blindauction.blindauctionshopproject.entity.Admin;
import com.blindauction.blindauctionshopproject.entity.AdminRoleEnum;

import com.blindauction.blindauctionshopproject.entity.User;
import com.blindauction.blindauctionshopproject.repository.AdminRepository;

import com.blindauction.blindauctionshopproject.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AdminService {
    private UserRepository userRepository;
    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;
    private static final String ADMIN_TOKEN = "eyJzdWIiOiJoZWxsb3dvcmxkIiwibm";
        
    @Transactional
    public void signupAdmin(AdminSignupRequest adminSignupRequest) {
        String username = adminSignupRequest.getUsername();
        String nickname = adminSignupRequest.getNickname();
        String password = passwordEncoder.encode(adminSignupRequest.getPassword());

        Optional<Admin> found = adminRepository.findByUsername(username);
        if(found.isPresent()){
            throw new IllegalArgumentException("중복된 관리자 아이디가 존재합니다.");
        }
        if(!adminSignupRequest.getAdminToken().equals(ADMIN_TOKEN)){
            throw new IllegalArgumentException("관리자 토큰이 일치하지 않습니다");
        }
        AdminRoleEnum role = AdminRoleEnum.ADMIN;

        Admin admin = new Admin(username,nickname,password,role);
        adminRepository.save(admin);
    }
        

    public List<UserResponse> getUserList(User user) {

        user = userRepository.findByUsername(user.getUsername()).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 아이디입니다.")
        );

        UserResponse userResponse = new UserResponse(user.getUsername(), user.getNickname());
        List<UserResponse> userResponseList = new ArrayList<>();

        userResponseList.add(userResponse);

        return userResponseList; // 페이징처리.. 어떻게?
    }
}
