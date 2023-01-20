package com.blindauction.blindauctionshopproject.dto.admin;

import com.blindauction.blindauctionshopproject.entity.User;
import lombok.Getter;

@Getter
public class UserResponse {

    String username;

    String nickname;

    public UserResponse(User user) {
        this.username = user.getUsername();
        this.nickname = user.getNickname();
    }
}
