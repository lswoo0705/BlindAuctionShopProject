package com.blindauction.blindauctionshopproject.dto.user;

import lombok.Getter;

@Getter
public class SellerResponse {
    String username;
    String nickname;
    String sellerDetail;

    public SellerResponse(String username, String nickname, String sellerDetail) {
        this.username = username;
        this.nickname = nickname;
        this.sellerDetail = sellerDetail;
    }
}
