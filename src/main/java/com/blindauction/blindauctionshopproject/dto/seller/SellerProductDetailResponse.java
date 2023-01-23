package com.blindauction.blindauctionshopproject.dto.seller;

import com.blindauction.blindauctionshopproject.entity.Product;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class SellerProductDetailResponse {
    private final String title;
    private final Long price;
    private final String productDetail;
    private List<PurchasePermissionResponse> bidderList;

    public SellerProductDetailResponse(Product product, List<PurchasePermissionResponse> bidderList) {
        this.title = product.getTitle();
        this.price = product.getPrice();
        this.productDetail = product.getProductDetail();
        this.bidderList = new ArrayList<>(bidderList);
    }
    public SellerProductDetailResponse(Product product) {
        this.title = product.getTitle();
        this.price = product.getPrice();
        this.productDetail = product.getProductDetail();
    }
}
