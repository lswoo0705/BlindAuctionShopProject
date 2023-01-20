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

    public SellerProductResponse(Long productId, String title, Long price, String productDetail, int bidderCnt) {
        this.productId = productId;
        this.title = title;
        this.price = price;
        this.productDetail = productDetail;
        this.bidderCnt = bidderCnt;
    }
}
