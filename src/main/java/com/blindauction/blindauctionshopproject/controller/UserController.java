package com.blindauction.blindauctionshopproject.controller;

import com.blindauction.blindauctionshopproject.dto.StatusResponse;
import com.blindauction.blindauctionshopproject.dto.StatusResponseDto;
import com.blindauction.blindauctionshopproject.dto.user.UserSignupRequest;
import com.blindauction.blindauctionshopproject.service.UserService;
import com.blindauction.blindauctionshopproject.util.jwtUtil.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.nio.charset.StandardCharsets;

@RequiredArgsConstructor
@RestController
public class UserController {
    private final JwtUtil jwtUtil;
    private final UserService userService;

    public ResponseEntity<StatusResponse> signupUser (@RequestBody @Valid UserSignupRequest userSignupRequest){
        StatusResponse statusResponse = new StatusResponse(HttpStatus.CREATED.value(), "회원가입 완료");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("application", "json", StandardCharsets.UTF_8));

        userService.signupUser(userSignupRequest);
        return new ResponseEntity<>(statusResponse, headers, HttpStatus.CREATED);
    }

}
