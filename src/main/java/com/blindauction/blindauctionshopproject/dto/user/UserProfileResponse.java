package com.blindauction.blindauctionshopproject.dto.user;

import lombok.Getter;

@Getter
public class UserProfileResponse {
    private String username;
    private String nickname;

    public UserProfileResponse(String username, String nickname) {
        this.username = username;
        this.nickname = nickname;
    }
}
