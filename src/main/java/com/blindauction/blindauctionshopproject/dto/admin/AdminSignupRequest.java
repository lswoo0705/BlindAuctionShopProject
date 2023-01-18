package com.blindauction.blindauctionshopproject.dto.admin;

import lombok.Getter;

@Getter
public class AdminSignupRequest { // 관리자 회원가입 request dto
    public String username;
    public String nickname;
    public String password;
    public String adminToken;

}
