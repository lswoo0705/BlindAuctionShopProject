package com.blindauction.blindauctionshopproject.dto.product;

import lombok.Getter;

@Getter
public class PurchaseRequest {
    private final String msg;
    private final Long price;

    public PurchaseRequest(String msg, Long price) {
        this.msg = msg;
        this.price = price;
    }
}
