package com.blindauction.blindauctionshopproject.entity;

import lombok.Getter;

public enum PermissionStatusEnum {
    WAITING("WAITING"),
    ACCEPTANCE("ACCEPTANCE"),
    REFUSAL("REFUSAL");

    @Getter
    private final String status;

    PermissionStatusEnum(String status){
        this.status = status;
    }




}
