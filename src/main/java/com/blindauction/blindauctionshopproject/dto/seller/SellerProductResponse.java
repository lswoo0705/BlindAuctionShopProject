package com.blindauction.blindauctionshopproject.dto.seller;

import com.blindauction.blindauctionshopproject.entity.Product;
import lombok.Getter;

@Getter
public class SellerProductResponse {
    private final Long productId;
    private final String title;
    private final Long price;
    private final String productDetail;
    private final int bidderCnt;

    public SellerProductResponse(Product product) {
        this.productId = product.getId();
        this.title = product.getTitle();
        this.price = product.getPrice();
        this.productDetail = product.getProductDetail();
        this.bidderCnt = product.getBidderCnt();
    }
}
