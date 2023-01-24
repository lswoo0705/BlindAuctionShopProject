package com.blindauction.blindauctionshopproject.controller;

import com.blindauction.blindauctionshopproject.dto.admin.*;
import com.blindauction.blindauctionshopproject.dto.security.StatusResponse;
import com.blindauction.blindauctionshopproject.dto.security.UsernameAndRoleResponse;
import com.blindauction.blindauctionshopproject.dto.admin.AdminSignupRequest;
import com.blindauction.blindauctionshopproject.dto.admin.SellerDetailResponse;
import com.blindauction.blindauctionshopproject.dto.admin.SellerPermissionResponse;
import com.blindauction.blindauctionshopproject.dto.admin.UserResponse;
import com.blindauction.blindauctionshopproject.service.AdminService;
import com.blindauction.blindauctionshopproject.util.jwtUtil.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.nio.charset.StandardCharsets;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final JwtUtil jwtUtil;
    private final AdminService adminService;

    @PostMapping("/signup") // 관리자 회원가입
    public ResponseEntity<StatusResponse> signupAdmin(@RequestBody @Valid AdminSignupRequest adminSignupRequest) { // @Valid : @pattern 등 값제한 어노테이션 사용 위해 필요
        StatusResponse statusResponse = new StatusResponse(HttpStatus.CREATED.value(), "관리자 회원가입 완료"); // httpStatus.0000 들은 안에 int value , httpstatus.series series, String reasonPhrase 필드 3개 있는데 그중 value( 500, 200 ) 만 가져오는거
        HttpHeaders headers = new HttpHeaders(); //httpHeader 란 ? 클라이언트 - 서버 간 요청 또는 응답에 부가적 정보를 주고받을 수 있는 문자열
        headers.setContentType(new MediaType("application", "json", StandardCharsets.UTF_8)); // 위에서 만든 헤더에 담을 Content 의 type 을 정의

        adminService.signupAdmin(adminSignupRequest); // userService 에서 회원가입 기능 작동
        return new ResponseEntity<>(statusResponse, headers, HttpStatus.CREATED);
    }

    @GetMapping("/users") // 회원목록 조회
    public Page<UserResponse> getUserList() {
        return adminService.getUserList();
    }

    @GetMapping("/sellers") // 판매자목록 조회
    public Page<SellerDetailResponse> getSellerList() { return adminService.getSellerList();
    }

    @GetMapping("/seller-permissions") // 판매자권한 요청목록 전체조회
    public Page<SellerPermissionResponse> getSellerPermissionList() {
        return adminService.getSellerPermissionList();
    }

    ////////////////////////////////////////////////////////////////////////// ▼ 대공사 존

    //1. 특정 판매자 권한 요청을 조회
    @GetMapping("seller-permissions/{sellerPermissionId}")
    public SellerPermissionResponse getSellerPermissionDetail(@PathVariable Long sellerPermissionId){
        return adminService.getSellerPermissionDetail(sellerPermissionId);
    }

    //2. 그 특정 판매자 권한 요청을 볼때 그걸 ACCEPT & REFUSAL 하는 기능
    @PutMapping("seller-permissions/{sellerPermissionId}")
    public ResponseEntity<StatusResponse> AnswerSellerPermission(@RequestBody AnswerSellerPermissionRequest request, @PathVariable Long sellerPermissionId){
        String msg = adminService.AnswerSellerPermissionDetail(request, sellerPermissionId);
        return ResponseEntity.ok().body(new StatusResponse(HttpStatus.OK.value(), msg));
    }
    //3. 그냥 ROLE/USERID 로 특정 유저 권한만 SELLER바꾸는거
    @PutMapping("/role/{userId}") // USER을 SELLER로 권한 전환
    public ResponseEntity<StatusResponse> acceptSellerRole(@PathVariable Long userId) {
        adminService.acceptSellerRole(userId);
        return ResponseEntity.accepted().body(new StatusResponse(HttpStatus.ACCEPTED.value(), "권한 승인"));
    }
    //4. 그냥 ROLE/USERID로 특정 유저 권한 강등
    @PutMapping("/role/delete/{userId}") // SELLER를 USER로 권한 전환
    public ResponseEntity<StatusResponse> deleteSellerRole(@PathVariable Long userId) {
        adminService.deleteSellerRole(userId);
        return ResponseEntity.accepted().body(new StatusResponse(HttpStatus.ACCEPTED.value(), "권한 삭제"));
    }
    //////////////////////////////////////////////////////////////////////////// ▲ 대공사 존
    //관리자 로그인
    @PostMapping("/login")
    public ResponseEntity<StatusResponse> loginAdmin(@RequestBody AdminLoginRequest adminLoginRequest, HttpServletResponse response){
        StatusResponse statusResponse = new StatusResponse(HttpStatus.OK.value(), "관리자 로그인 완료");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("application", "json", StandardCharsets.UTF_8));
        UsernameAndRoleResponse adminUsernameAndRoleResponse = adminService.loginAdmin(adminLoginRequest);
        response.addHeader(JwtUtil.AUTHORIZATION_HEADER,jwtUtil.createToken(adminUsernameAndRoleResponse.getUsername(), adminUsernameAndRoleResponse.getRole())); // 헤더에 jwt 인증 토큰 담음

        return new ResponseEntity<>(statusResponse, headers, HttpStatus.OK); // http 상태 정보, jwt 토큰이 담긴 헤더 를 리턴함
    }

    //관리자 로그아웃
    @PostMapping("/logout") // 로그인 토큰 (프론트) : 쿠키나 기타 장소에 프론트가 들고있음. 인증이 필요한 기능쓸때마다 쿠키에서 토큰 꺼내옴. 서버 : 프론트한테 쿠키를 삭제하게끔... 프론트 : 쿠키안에 있던 토큰이 사라져서 인가가 안됨! (2.이외에 redis 방법 있음) 1. 리프레시토큰쓰는방법.
    public ResponseEntity<StatusResponse> logoutAdmin(HttpServletResponse response) {
        StatusResponse statusResponse = new StatusResponse(HttpStatus.OK.value(), "로그아웃 완료");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("application", "json", StandardCharsets.UTF_8));
        response.setHeader(JwtUtil.AUTHORIZATION_HEADER, "");
        return new ResponseEntity<>(statusResponse, headers, HttpStatus.OK);
    }
}
