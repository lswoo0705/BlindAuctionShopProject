package com.blindauction.blindauctionshopproject.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity(name="PRODUCT")
public class Product extends TimeStamped{
    @Id
    private long id;

    @JoinColumn(name = "nickname", nullable = false)  // 연관관계 다시 확인
    @ManyToOne
    private User seller;
    private String title;
    private Long price;
    private String productDetail;
    private int bidderCnt;

    public Product(String title, Long price, String productDetail) {
        this.title = title;
        this.price = price;
        this.productDetail = productDetail;
    }

    public void update(String title, Long price, String productDetail) {
        this.title = title;
        this.price = price;
        this.productDetail = productDetail;
    }
}
