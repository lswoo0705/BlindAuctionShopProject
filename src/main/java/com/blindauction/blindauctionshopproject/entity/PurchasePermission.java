package com.blindauction.blindauctionshopproject.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;

@Getter
@NoArgsConstructor
@Entity(name="PURCHASE_PERMISSION")
public class PurchasePermission {
    @Id
    private long id; //구매신청 id
    private Product product; //원본 판매글
    private User bidder; //구매신청자
    private Long price; //제시한 가격
    private TransactionStatusEnum transactionStatus; //구매신청 처리상태

}
