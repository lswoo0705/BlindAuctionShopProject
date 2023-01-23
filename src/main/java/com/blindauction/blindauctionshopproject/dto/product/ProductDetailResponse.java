package com.blindauction.blindauctionshopproject.dto.product;

import com.blindauction.blindauctionshopproject.entity.Product;
import lombok.Getter;

@Getter
public class ProductDetailResponse {
    private final Long productId;
    private final String title;
    private final Long price;
    private final String nickname;
    private final int bidderCnt;

    public ProductDetailResponse(Product product) {
        this.productId = product.getId(); // 판매 상품의 아이디
        this.title = product.getTitle(); // 판매 상품의 이름
        this.price = product.getPrice(); // 판매 상품의 가격
        this.nickname = product.getSeller().getNickname(); // 판매 상품의 판매자
        this.bidderCnt = product.getBidderCnt(); // 판매 상품의 입찰자 수
    }
}
