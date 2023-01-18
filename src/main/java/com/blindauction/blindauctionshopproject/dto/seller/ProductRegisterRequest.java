package com.blindauction.blindauctionshopproject.dto.seller;

import com.blindauction.blindauctionshopproject.entity.Product;
import lombok.Getter;

@Getter
public class ProductRegisterRequest {
    private final String title;
    private final Long price;
    private final String productDetail;

    public ProductRegisterRequest(Product product) {
        this.title = product.getTitle();
        this.price = product.getPrice();
        this.productDetail = product.getProductDetail();
    }
}
