package com.blindauction.blindauctionshopproject.dto.user;

import com.blindauction.blindauctionshopproject.entity.PurchasePermission;
import com.blindauction.blindauctionshopproject.entity.TransactionStatusEnum;
import lombok.Getter;

@Getter
public class PurchaseStatusGetResponse {
    private Long productId;
    private String title;
    private TransactionStatusEnum transactionStatusEnum;

    public PurchaseStatusGetResponse(PurchasePermission purchasePermission) {
        this.productId = purchasePermission.getProduct().getId();
        this.title = purchasePermission.getProduct().getTitle();
        this.transactionStatusEnum = purchasePermission.getTransactionStatus();
    }
}
