package com.blindauction.blindauctionshopproject.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity(name="PRODUCT")
public class Product extends TimeStamped{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @JoinColumn(name = "nickname", nullable = false)  // 연관관계 다시 확인
    @ManyToOne
    private User seller;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private Long price;

    @Column(nullable = false)
    private String productDetail;

    @Column(nullable = false)
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
