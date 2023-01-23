package com.blindauction.blindauctionshopproject.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity(name = "PURCHASE_PERMISSION")
public class PurchasePermission {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id; //구매신청 id

    @JoinColumn(nullable = false)  // 연관관계 다시 확인
    @ManyToOne(cascade = CascadeType.REMOVE)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Product product; //원본 판매글

    @JoinColumn(nullable = false)  // 연관관계 다시 확인
    @ManyToOne(fetch = FetchType.LAZY)
    private User bidder; //구매신청자

    @Column(nullable = false)
    private String msg; // 구매신청글

    @Column(nullable = false)
    private Long price; //제시한 가격

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private PermissionStatusEnum transactionStatus; //구매신청 처리상태

    public boolean checkStatusIsWaiting() {
        return this.getTransactionStatus().equals(PermissionStatusEnum.WAITING);
    }

    public void updateStatus(PermissionStatusEnum permissionStatusEnum) {
        this.transactionStatus = permissionStatusEnum;
    }

    // 생성자
    public PurchasePermission(Product product, User bidder, String msg, Long price, PermissionStatusEnum transactionStatus) {
        this.product = product;
        this.bidder = bidder;
        this.msg = msg;
        this.price = price;
        this.transactionStatus = transactionStatus;
    }
}
