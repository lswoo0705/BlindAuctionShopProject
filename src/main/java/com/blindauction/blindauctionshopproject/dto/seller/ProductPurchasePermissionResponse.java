package com.blindauction.blindauctionshopproject.dto.seller;

import lombok.Getter;

import java.util.List;

@Getter
public class ProductPurchasePermissionResponse {
    private final Long productId;
    private final String title;
    private final Long price;
    private final List<PurchasePermissionResponse> bidderList;

    public ProductPurchasePermissionResponse(Long productId, String title, Long price, List<PurchasePermissionResponse> bidderList) {
        this.productId = productId;
        this.title = title;
        this.price = price;
        this.bidderList = bidderList;
    }
}