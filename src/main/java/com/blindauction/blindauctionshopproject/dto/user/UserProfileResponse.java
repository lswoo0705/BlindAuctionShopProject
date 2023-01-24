package com.blindauction.blindauctionshopproject.dto.user;

import lombok.Getter;

@Getter
public class UserProfileResponse {
    private final String username;
    private final String nickname;

    public UserProfileResponse(String username, String nickname) {
        this.username = username;
        this.nickname = nickname;
    }
}
