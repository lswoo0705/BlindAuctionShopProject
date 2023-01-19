package com.blindauction.blindauctionshopproject.dto.seller;

import com.blindauction.blindauctionshopproject.entity.Product;
import lombok.Getter;

@Getter
public class ProductPurchasePermissionResponse {
    private final Long productId;
    private final String title;
    private final Long price;

    public ProductPurchasePermissionResponse(Product product) {
        this.productId = product.getId();
        this.title = product.getTitle();
        this.price = product.getPrice();
    }
}