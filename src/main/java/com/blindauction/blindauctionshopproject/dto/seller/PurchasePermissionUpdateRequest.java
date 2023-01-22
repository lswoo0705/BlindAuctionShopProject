package com.blindauction.blindauctionshopproject.dto.seller;

import com.blindauction.blindauctionshopproject.entity.TransactionStatusEnum;
import lombok.Getter;

@Getter
public class PurchasePermissionUpdateRequest {
    public TransactionStatusEnum transactionStatus;
}