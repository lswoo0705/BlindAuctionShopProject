package com.blindauction.blindauctionshopproject.controller;

import com.blindauction.blindauctionshopproject.dto.security.StatusResponse;
import com.blindauction.blindauctionshopproject.dto.security.UsernameAndRoleResponse;
import com.blindauction.blindauctionshopproject.dto.user.*;
import com.blindauction.blindauctionshopproject.dto.user.UserProfileResponse;
import com.blindauction.blindauctionshopproject.service.UserService;
import com.blindauction.blindauctionshopproject.util.jwtUtil.JwtUtil;
import com.blindauction.blindauctionshopproject.util.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.nio.charset.StandardCharsets;


@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {
    private final JwtUtil jwtUtil;
    private final UserService userService;

    // 일반 회원가입 [확인ㅇ]
    @PostMapping("/signup")
    public ResponseEntity<StatusResponse> signupUser(@RequestBody @Valid UserSignupRequest userSignupRequest) {
        StatusResponse statusResponse = new StatusResponse(HttpStatus.CREATED.value(), "회원가입 완료");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("application", "json", StandardCharsets.UTF_8));

        userService.signupUser(userSignupRequest);
        return new ResponseEntity<>(statusResponse, headers, HttpStatus.CREATED);
    }

    // 일반 로그인 [확인ㅇ]
    @PostMapping("/login")
    public ResponseEntity<StatusResponse> loginUser(@RequestBody UserLoginRequest userLoginRequest, HttpServletResponse response) {
        StatusResponse statusResponse = new StatusResponse(HttpStatus.OK.value(), "로그인 완료"); // resposneEntity 용 http 상태 정보 담긴 dto 생성
        HttpHeaders headers = new HttpHeaders(); // 헤더 생성
        headers.setContentType(new MediaType("application", "json", StandardCharsets.UTF_8)); // header 의 정보 타입 지정
        UsernameAndRoleResponse usernameAndRoleResponse = userService.loginUser(userLoginRequest); // 로그인 기능 수행 후 결과를 usernameAndRoleResponse Entity 에 담음
        response.addHeader(JwtUtil.AUTHORIZATION_HEADER, jwtUtil.createToken(usernameAndRoleResponse.getUsername(), usernameAndRoleResponse.getRole())); // 헤더에 jwt 인증 토큰 담음
        return new ResponseEntity<>(statusResponse, headers, HttpStatus.OK); // http 상태 정보, jwt 토큰이 담긴 헤더 를 리턴함
    }

    // 일반 로그아웃
    @PostMapping("/logout")
    public ResponseEntity<StatusResponse> logoutUser(HttpServletRequest request, HttpServletResponse response) {
        String token = request.getHeader(JwtUtil.AUTHORIZATION_HEADER);
        userService.logoutUser(token);

        StatusResponse statusResponse = new StatusResponse(HttpStatus.OK.value(), "로그아웃 완료");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("application", "json", StandardCharsets.UTF_8));
        response.setHeader(JwtUtil.AUTHORIZATION_HEADER, "");


        return new ResponseEntity<>(statusResponse, headers, HttpStatus.OK);
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
    public ResponseEntity<StatusResponse> registerSellerPermission(@RequestBody SellerPermissionRegisterRequest sellerPermissionRegisterRequest,
                                                                   @AuthenticationPrincipal UserDetailsImpl userDetails) {
        //1.userDetails 에서 username 꺼내기
        String username = userDetails.getUsername();

        //2. Request에서 phonNum 이랑 Detail 꺼내서 서비스로 보내기
        String phoneNum = sellerPermissionRegisterRequest.getPhoneNum();
        String permissionDetail = sellerPermissionRegisterRequest.getPermissionDetail();

        //3. 판매자 등록요청이 잘 됐다고 메세지 보내기
        StatusResponse statusResponse = new StatusResponse(HttpStatus.OK.value(), "판매자 등록 신청 완료");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("application", "json", StandardCharsets.UTF_8));

        userService.registerSellerPermission(phoneNum, permissionDetail, username);

        return new ResponseEntity<>(statusResponse, headers, HttpStatus.OK);
    }

    // 판매자 목록 조회
    @GetMapping("/sellers-list")
    public Page<SellerResponse> getSellerList() {
        return userService.getSellerList();
    }

    // 판매자 개별 조회
    @GetMapping("/sellers-list/{userId}")
    public SellerResponse getSellerById(@PathVariable Long userId) {
        return userService.getSellerById(userId);
    }

    // 나의 전체 구매신청 상태 조회
    @GetMapping("/purchase-permission")
    public Page<PurchaseStatusGetResponse> getPurchaseStatus(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                              @RequestParam int page) {
        String username = userDetails.getUsername();
        return userService.getPurchaseStatus(username, page - 1);
    }
}
