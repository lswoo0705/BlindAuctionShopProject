package com.blindauction.blindauctionshopproject.dto.user;

import lombok.Getter;

@Getter
public class UserProfileUpdateRequest {
    private final String nickname;
    private final String password;

    public UserProfileUpdateRequest(String nickname, String password) {
        this.nickname = nickname;
        this.password = password;
    }
}
