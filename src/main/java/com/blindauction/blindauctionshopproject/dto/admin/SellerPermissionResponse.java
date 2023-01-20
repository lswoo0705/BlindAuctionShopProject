package com.blindauction.blindauctionshopproject.dto.admin;

import com.blindauction.blindauctionshopproject.entity.SellerPermission;
import lombok.Getter;

@Getter
public class SellerPermissionResponse {

    String username;

    String nickname;

    String phoneNum;

    String permissionDetail;

    public SellerPermissionResponse(SellerPermission sellerPermission) {
        this.username = sellerPermission.getUser().getUsername();
        this.nickname = sellerPermission.getUser().getNickname();
        this.phoneNum = sellerPermission.getPhoneNum();
        this.permissionDetail = sellerPermission.getPermissionDetail();
    }
}
