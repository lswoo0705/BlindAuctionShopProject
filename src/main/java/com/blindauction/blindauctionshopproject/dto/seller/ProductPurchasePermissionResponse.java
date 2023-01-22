package com.blindauction.blindauctionshopproject.dto.seller;

import com.blindauction.blindauctionshopproject.entity.Product;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class ProductPurchasePermissionResponse {
    private final Long productId;
    private final String title;
    private final Long price;
    private final List<PurchasePermissionResponse> bidderList;

    public ProductPurchasePermissionResponse(Product product, List<PurchasePermissionResponse> bidderList) {
        this.productId = product.getId();
        this.title = product.getTitle();
        this.price = product.getPrice();
        this.bidderList = new ArrayList<>(bidderList);
    }
}