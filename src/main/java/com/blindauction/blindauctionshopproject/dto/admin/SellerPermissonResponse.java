package com.blindauction.blindauctionshopproject.dto.admin;

import lombok.Getter;

@Getter
public class SellerPermissonResponse {

    String username;

    String nickname;

    String phoneNum;

    String permissionDetail;

    public SellerPermissonResponse(String username, String nickname, String phoneNum, String permissionDetail) {
        this.username = username;
        this.nickname = nickname;
        this.phoneNum = phoneNum;
        this.permissionDetail = permissionDetail;
    }
}
