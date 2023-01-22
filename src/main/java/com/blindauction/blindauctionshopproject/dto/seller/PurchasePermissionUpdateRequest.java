package com.blindauction.blindauctionshopproject.dto.seller;

import com.blindauction.blindauctionshopproject.entity.TransactionStatusEnum;
import lombok.Getter;

import javax.validation.constraints.NotBlank;

@Getter
public class PurchasePermissionUpdateRequest {
    //accept or afusal 이거아님 안됨.
    @NotBlank(message = "거래상태 값 입력은 필수입니다.")
    private final TransactionStatusEnum transactionStatus;

    public PurchasePermissionUpdateRequest(TransactionStatusEnum transactionStatus) {
        this.transactionStatus = transactionStatus;
    }
}