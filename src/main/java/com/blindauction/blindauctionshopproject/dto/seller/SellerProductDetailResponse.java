package com.blindauction.blindauctionshopproject.dto.seller;

import com.blindauction.blindauctionshopproject.entity.Product;
import lombok.Getter;

import java.util.List;

@Getter
public class SellerProductDetailResponse {
    private final String title;
    private final Long price;
    private final String productDetail;
    private final List<PurchasePermissionResponse> bidderList;

    public SellerProductDetailResponse(String title, Long price, String productDetail, List<PurchasePermissionResponse> bidderList) {
        this.title = title;
        this.price = price;
        this.productDetail = productDetail;
        this.bidderList = bidderList;
    }
}
