package com.blindauction.blindauctionshopproject.controller;

import com.blindauction.blindauctionshopproject.dto.StatusResponse;
import com.blindauction.blindauctionshopproject.dto.user.*;
import com.blindauction.blindauctionshopproject.dto.user.UserProfileResponse;
import com.blindauction.blindauctionshopproject.service.UserService;
import com.blindauction.blindauctionshopproject.util.jwtUtil.JwtUtil;
import com.blindauction.blindauctionshopproject.util.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.nio.charset.StandardCharsets;
import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {
    private final JwtUtil jwtUtil;
    private final UserService userService;

// 회원가입
    public ResponseEntity<StatusResponse> signupUser(@RequestBody @Valid UserSignupRequest userSignupRequest) {
        StatusResponse statusResponse = new StatusResponse(HttpStatus.CREATED.value(), "회원가입 완료");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("application", "json", StandardCharsets.UTF_8));

        userService.signupUser(userSignupRequest);
        return new ResponseEntity<>(statusResponse, headers, HttpStatus.CREATED);
    }

    // 나의 프로필 조회
    @GetMapping("/profile")
    public UserProfileResponse getUserProfile(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        String userInfo = userDetails.getUsername();
        return userService.getUserProfile(userInfo);
    }

    // 나의 프로필 설정(수정)
    @PutMapping("/profile")
    public ResponseEntity<StatusResponse> updateUserProfile(@RequestBody UserProfileUpdateRequest userProfileUpdateRequest,
                                                            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        String nickname = userProfileUpdateRequest.getNickname();
        String password = userProfileUpdateRequest.getPassword();

        String userInfo = userDetails.getUsername();

        StatusResponse statusResponse = new StatusResponse(HttpStatus.OK.value(), "프로필 설정 완료");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("application", "json", StandardCharsets.UTF_8));

        userService.updateUserProfile(nickname, password, userInfo);

        return new ResponseEntity<>(statusResponse, headers, HttpStatus.OK);
    }

    // 판매자 등록 요청
    @PostMapping("/seller-permission")
    public ResponseEntity<StatusResponse> registerSellerPermission(@RequestBody SellerPermissionRegisterRequest
                                                                           sellerPermissionRegisterRequest) {
        String phoneNum = sellerPermissionRegisterRequest.getPhoneNum();
        String permissionDetail = sellerPermissionRegisterRequest.getPermissionDetail();

        StatusResponse statusResponse = new StatusResponse(HttpStatus.OK.value(), "판매자 등록 신청 완료");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("application", "json", StandardCharsets.UTF_8));

        userService.registerSellerPermission(phoneNum, permissionDetail);

        return new ResponseEntity<>(statusResponse, headers, HttpStatus.OK);
    }

    @GetMapping("/sellers-list") // 페이징 타입으로?
    // 페이징 : 페이지 타입으로 반환하면 리스트에 담겨져서 나오는지?
    public List<SellerResponse> getSellerList() {
        return userService.getSellerList();
    }

    @GetMapping("/sellers-list/{userId}")
    public SellerResponse getSellerById(@PathVariable Long userId) {
        return userService.getSellerById(userId);
    }
}
