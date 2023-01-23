package com.blindauction.blindauctionshopproject.dto.security;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class StatusResponse {
    private final int statusCode; // 예시 200

    private final String message; // 예시 회원가입완료


}
