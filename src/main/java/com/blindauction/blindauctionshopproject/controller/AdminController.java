package com.blindauction.blindauctionshopproject.controller;

import com.blindauction.blindauctionshopproject.dto.StatusResponse;
import com.blindauction.blindauctionshopproject.dto.admin.AdminSignupRequest;
import com.blindauction.blindauctionshopproject.dto.admin.SellerDetailResponse;
import com.blindauction.blindauctionshopproject.dto.admin.SellerPermissonResponse;
import com.blindauction.blindauctionshopproject.dto.admin.UserResponse;
import com.blindauction.blindauctionshopproject.service.AdminService;
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
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final JwtUtil jwtUtil;
    private final AdminService adminService;

    @PostMapping("/admin/signup")
    public ResponseEntity<StatusResponse> signupAdmin (@RequestBody @Valid AdminSignupRequest adminSignupRequest) { // @Valid : @pattern 등 값제한 어노테이션 사용 위해 필요
        StatusResponse statusResponse = new StatusResponse(HttpStatus.CREATED.value(), "관리자 회원가입 완료"); // httpStatus.0000 들은 안에 int value , httpstatus.series series, String reasonPhrase 필드 3개 있는데 그중 value( 500, 200 ) 만 가져오는거
        HttpHeaders headers = new HttpHeaders(); //httpHeader 란 ? 클라이언트 - 서버 간 요청 또는 응답에 부가적 정보를 주고받을 수 있는 문자열
        headers.setContentType(new MediaType("application", "json", StandardCharsets.UTF_8)); // 위에서 만든 헤더에 담을 Content 의 type 을 정의

        adminService.signupAdmin(adminSignupRequest); // userService 에서 회원가입 기능 작동
        return new ResponseEntity<>(statusResponse, headers, HttpStatus.CREATED);
    }

    @GetMapping("/users")
    public List<UserResponse> getUserList(@AuthenticationPrincipal UserDetailsImpl userDetails) { // @AuthenticationPrincipal : 시큐리티를 사용한 인증/인가
        return adminService.getUserList(userDetails.getUser());
    }

    @GetMapping("/sellers")
    public List<SellerDetailResponse> getSellerList(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return adminService.getSellerList(userDetails.getUser());
    }

    @GetMapping("/seller-permissions")
    public List<SellerPermissonResponse> getSellerPermissionList(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return adminService.getSellerPermissionList(userDetails.getUser());
    }

    @PutMapping("/role/{userId}")
    public ResponseEntity<StatusResponse> acceptSellerRole(@AuthenticationPrincipal UserDetailsImpl userDetails, @PathVariable Long userId) {
        adminService.acceptSellerRole(userId, userDetails.getUser());
        return ResponseEntity.accepted().body(new StatusResponse(HttpStatus.ACCEPTED.value(), "권한 승인"));
    }
}
