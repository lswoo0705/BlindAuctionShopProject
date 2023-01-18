package com.blindauction.blindauctionshopproject.dto.admin;

import com.blindauction.blindauctionshopproject.entity.User;
import lombok.Getter;

@Getter
public class UserResponse {

    String username;

    String nickname;

    public UserResponse(String username, String nickname) {
        this.username = username;
        this.nickname = nickname;
    }
}
