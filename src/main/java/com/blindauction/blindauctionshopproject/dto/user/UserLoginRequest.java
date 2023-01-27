package com.blindauction.blindauctionshopproject.dto.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class UserLoginRequest {
    String username;
    String password;
}

