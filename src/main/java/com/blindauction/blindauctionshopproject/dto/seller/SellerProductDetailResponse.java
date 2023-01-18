package com.blindauction.blindauctionshopproject.dto.seller;

import com.blindauction.blindauctionshopproject.entity.Product;
import lombok.Getter;

@Getter
public class SellerProductDetailResponse {
    private final String title;
    private final Long price;
    private final String productDetail;
    private final int bidderList;

    public SellerProductDetailResponse(Product product) {
        this.title = product.getTitle();
        this.price = product.getPrice();
        this.productDetail = product.getProductDetail();
        this.bidderList = product.getBidderCnt();
    }
}
