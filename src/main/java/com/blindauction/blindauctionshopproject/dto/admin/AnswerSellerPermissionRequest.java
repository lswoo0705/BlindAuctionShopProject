package com.blindauction.blindauctionshopproject.dto.admin;

import com.blindauction.blindauctionshopproject.entity.TransactionStatusEnum;
import lombok.Getter;

@Getter
public class AnswerSellerPermissionRequest {
    private TransactionStatusEnum answer;
}
