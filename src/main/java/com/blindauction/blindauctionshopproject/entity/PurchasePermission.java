package com.blindauction.blindauctionshopproject.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity(name="PURCHASE_PERMISSION")
public class PurchasePermission {
    @Id
    private long id; //구매신청 id

    @JoinColumn(name = "title", nullable = false)  // 연관관계 다시 확인
    @ManyToOne
    private Product product; //원본 판매글

    @JoinColumn(name = "nickname", nullable = false)  // 연관관계 다시 확인
    @ManyToOne
    private User bidder; //구매신청자 ( perchasePermission 작성자 )
    private Long price; //제시한 가격
    private TransactionStatusEnum transactionStatus; //구매신청 처리상태

}
