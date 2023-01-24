package com.blindauction.blindauctionshopproject.dto.seller;

import com.blindauction.blindauctionshopproject.entity.PurchasePermission;
import com.blindauction.blindauctionshopproject.entity.PermissionStatusEnum;
import lombok.Getter;

@Getter
public class PurchasePermissionResponse {
    private final String username;
    private final String bidder;
    private final String msg;
    private final Long price;
    private final PermissionStatusEnum permissionStatusEnum;

    public PurchasePermissionResponse(PurchasePermission purchasePermission){
        this.username = purchasePermission.getBidder().getUsername();
        this.bidder = purchasePermission.getBidder().getNickname();
        this.msg = purchasePermission.getMsg();
        this.price = purchasePermission.getPrice();
        this.permissionStatusEnum = purchasePermission.getTransactionStatus();
    }

}