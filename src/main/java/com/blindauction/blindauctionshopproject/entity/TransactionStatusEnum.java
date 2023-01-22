package com.blindauction.blindauctionshopproject.entity;

import lombok.Getter;

public enum TransactionStatusEnum {
    WAITING("WAITING"),
    ACCEPTANCE("ACCEPTANCE"),
    REFUSAL("REFUSAL");

    @Getter
    private final String status;

    TransactionStatusEnum(String status){
        this.status = status;
    }




}
