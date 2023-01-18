package com.blindauction.blindauctionshopproject.controller;

import com.blindauction.blindauctionshopproject.dto.StatusResponse;
import com.blindauction.blindauctionshopproject.dto.user.*;
import com.blindauction.blindauctionshopproject.dto.user.SellerPermissionRegisterRequest;
import com.blindauction.blindauctionshopproject.dto.user.UserProfileResponse;
import com.blindauction.blindauctionshopproject.service.UserService;
import com.blindauction.blindauctionshopproject.util.jwtUtil.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.nio.charset.StandardCharsets;
import java.security.Principal;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {
  private final JwtUtil jwtUtil;
    private final UserService userService;
      // 회원가입 완료
        public ResponseEntity<StatusResponse> signupUser (@RequestBody @Valid UserSignupRequest userSignupRequest){
        StatusResponse statusResponse = new StatusResponse(HttpStatus.CREATED.value(), "회원가입 완료");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("application", "json", StandardCharsets.UTF_8));

        userService.signupUser(userSignupRequest);
        return new ResponseEntity<>(statusResponse, headers, HttpStatus.CREATED);

     // 나의 프로필 조회
    @PostMapping("/profile")
    public UserProfileResponse getUserProfile(Principal principal) {
        userService.getUserProfile(principal.getName())
    }

    // 판매자 등록 요청
    @PostMapping("/seller-permission")
    public ResponseEntity<StatusResponse> registerSellerPermission(@RequestBody SellerPermissionRegisterRequest sellerPermissionRegisterRequest) {
        String phoneNum = sellerPermissionRegisterRequest.getPhoneNum();
        String permissionDetail = sellerPermissionRegisterRequest.getPermissionDetail();

        StatusResponse statusResponse = new StatusResponse(HttpStatus.OK.value(), "판매자 등록 신청 완료");

        userService.registerSellerPermission(phoneNum, permissionDetail);
        return new ResponseEntity<>("성공", HttpStatus.OK.valueOf());
    }

}
