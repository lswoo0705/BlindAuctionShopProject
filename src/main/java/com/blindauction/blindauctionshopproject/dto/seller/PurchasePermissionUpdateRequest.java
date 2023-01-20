package com.blindauction.blindauctionshopproject.dto.seller;

import com.blindauction.blindauctionshopproject.entity.TransactionStatusEnum;
import lombok.Getter;

@Getter
public class PurchasePermissionUpdateRequest {
    //accept or afusal 이거아님 안됨.
    private final TransactionStatusEnum transactionStatus;

    public PurchasePermissionUpdateRequest(TransactionStatusEnum transactionStatus) {
        this.transactionStatus = transactionStatus;
    }
}