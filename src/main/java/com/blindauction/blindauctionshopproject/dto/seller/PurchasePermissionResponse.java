package com.blindauction.blindauctionshopproject.dto.seller;

import com.blindauction.blindauctionshopproject.entity.PurchasePermission;
import lombok.Getter;

@Getter
public class PurchasePermissionResponse {
    private final String username;
    private final String bidder;
    private final String msg;
    private final Long price;

    public PurchasePermissionResponse(String username, String bidder, String msg, Long price) {
        this.username = username;
        this.bidder = bidder;
        this.msg = msg;
        this.price = price;
    }

    public PurchasePermissionResponse(PurchasePermission purchasePermission){
        this.username = purchasePermission.getBidder().getUsername();
        this.bidder = purchasePermission.getBidder().getNickname();
        this.msg = purchasePermission.getMsg();
        this.price = purchasePermission.getPrice();
    }
}