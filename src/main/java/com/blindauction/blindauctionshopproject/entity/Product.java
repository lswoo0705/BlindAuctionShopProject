package com.blindauction.blindauctionshopproject.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;

@Getter
@NoArgsConstructor
@Entity(name="PRODUCT")
public class Product extends TimeStamped{
    @Id
    private long id;
    private User seller;
    private String title;
    private Long price;
    private String productDetail;
    private int bidderCnt;


}
