package com.blindauction.blindauctionshopproject.dto.seller;

import com.blindauction.blindauctionshopproject.entity.PurchasePermission;
import com.blindauction.blindauctionshopproject.entity.User;
import lombok.Getter;

@Getter
public class PurchasePermissionResponse {
    private final String username;
    private final User bidder;
//    private final String msg;
    private final Long price;


    public PurchasePermissionResponse(User user, PurchasePermission purchasePermission) {
        this.username = user.getUsername();
        this.bidder = purchasePermission.getBidder();
//        this.msg = purchasePermission.getMsg();
        this.price = purchasePermission.getPrice();
    }
}