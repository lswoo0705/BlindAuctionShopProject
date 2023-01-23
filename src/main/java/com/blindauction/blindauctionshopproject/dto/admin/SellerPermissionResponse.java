package com.blindauction.blindauctionshopproject.dto.admin;

import com.blindauction.blindauctionshopproject.entity.SellerPermission;
import com.blindauction.blindauctionshopproject.entity.TransactionStatusEnum;
import lombok.Getter;

@Getter
public class SellerPermissionResponse {

    Long permissionId;
    String username;

    String nickname;

    String phoneNum;

    String permissionDetail;

    TransactionStatusEnum acceptanceStatus;

    public SellerPermissionResponse(SellerPermission sellerPermission) {
        this.permissionId = sellerPermission.getId();
        this.username = sellerPermission.getUser().getUsername();
        this.nickname = sellerPermission.getUser().getNickname();
        this.phoneNum = sellerPermission.getPhoneNum();
        this.permissionDetail = sellerPermission.getPermissionDetail();
        this.acceptanceStatus = sellerPermission.getAcceptance_status();
    }
}
