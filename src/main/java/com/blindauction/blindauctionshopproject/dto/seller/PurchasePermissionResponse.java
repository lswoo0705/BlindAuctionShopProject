package com.blindauction.blindauctionshopproject.dto.seller;

import lombok.Getter;

@Getter
public class PurchasePermissionResponse {
//    private final String username;

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
}