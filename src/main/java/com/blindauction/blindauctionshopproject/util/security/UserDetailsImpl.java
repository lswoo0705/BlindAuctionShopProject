package com.blindauction.blindauctionshopproject.util.security;

import com.blindauction.blindauctionshopproject.entity.User;

public class UserDetailsImpl {
    private final User user;

    public UserDetailsImpl(User user) {
        this.user = user;
    }

    public String getUsername() {
        return this.user.getUsername();
    }
}
