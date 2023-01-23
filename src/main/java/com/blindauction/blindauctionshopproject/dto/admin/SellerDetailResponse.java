package com.blindauction.blindauctionshopproject.dto.admin;

import lombok.Getter;

@Getter
public class SellerDetailResponse {

    String username;

    String nickname;

    String phoneNum;

    String sellerDetail;



    public SellerDetailResponse(String username, String nickname, String phoneNum, String sellerDetail) {
        this.username = username;
        this.nickname = nickname;
        this.phoneNum = phoneNum;
        this.sellerDetail = sellerDetail;
    }
}
