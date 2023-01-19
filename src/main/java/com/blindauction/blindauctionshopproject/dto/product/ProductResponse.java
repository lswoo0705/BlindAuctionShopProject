package com.blindauction.blindauctionshopproject.dto.product;

import com.blindauction.blindauctionshopproject.entity.Product;
import lombok.Getter;

@Getter
public class ProductResponse {
    private final Long productId;
    private final String title;
    private final Long price;

    public ProductResponse(Product product) {
        this.productId = product.getId();
        this.title = product.getTitle();
        this.price = product.getPrice();
    }
}
