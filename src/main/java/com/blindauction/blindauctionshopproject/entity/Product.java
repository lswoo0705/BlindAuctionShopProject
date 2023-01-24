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

    @ManyToOne // LAZY 때문에 null이 들어옴? 그래서 지웠습니다
    @JoinColumn(nullable = false)  // 연관관계 다시 확인
    private User seller;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private Long price;

    @Column(nullable = false)
    private String productDetail;

    @Column(nullable = false)
    private int bidderCnt;

    public Product(User seller, String title, Long price, String productDetail) {
        this.seller = seller;
        this.title = title;
        this.price = price;
        this.productDetail = productDetail;
        this.bidderCnt = 0;
    }

    public void plusBidderCnt(){
        this.bidderCnt += 1;
    }

    public void update(String title, Long price, String productDetail) {
        this.title = title;
        this.price = price;
        this.productDetail = productDetail;
    }

    public boolean checkUsernameIsProductSeller(String username) {
        return this.getSeller().getUsername().equals(username);
    }
}
