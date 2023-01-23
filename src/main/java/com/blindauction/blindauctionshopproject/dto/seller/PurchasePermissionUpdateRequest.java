package com.blindauction.blindauctionshopproject.dto.seller;

import com.blindauction.blindauctionshopproject.entity.PermissionStatusEnum;
import lombok.Getter;

@Getter
public class PurchasePermissionUpdateRequest {
    public PermissionStatusEnum transactionStatus;
}