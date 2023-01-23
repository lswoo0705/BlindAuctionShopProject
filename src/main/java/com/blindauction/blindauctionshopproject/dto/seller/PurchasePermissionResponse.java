package com.blindauction.blindauctionshopproject.dto.seller;

import com.blindauction.blindauctionshopproject.entity.PurchasePermission;
import com.blindauction.blindauctionshopproject.entity.TransactionStatusEnum;
import lombok.Getter;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Getter
public class PurchasePermissionResponse {
    private final String username;
    private final String bidder;
    private final String msg;
    private final Long price;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TransactionStatusEnum transactionStatusEnum;

    public PurchasePermissionResponse(PurchasePermission purchasePermission){
        this.username = purchasePermission.getBidder().getUsername();
        this.bidder = purchasePermission.getBidder().getNickname();
        this.msg = purchasePermission.getMsg();
        this.price = purchasePermission.getPrice();
        this.transactionStatusEnum = purchasePermission.getTransactionStatus();
    }

}