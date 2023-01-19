package com.blindauction.blindauctionshopproject.dto.seller;

import com.blindauction.blindauctionshopproject.entity.User;
import lombok.Getter;

@Getter
public class ProductDeleteRequest {
    private final String password;

    public ProductDeleteRequest(User user) {
        this.password = user.getPassword();
    }
}