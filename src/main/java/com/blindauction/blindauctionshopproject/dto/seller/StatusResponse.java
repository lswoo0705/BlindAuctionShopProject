package com.blindauction.blindauctionshopproject.dto.seller;

import lombok.Getter;

@Getter
public class StatusResponse {
    private final String msg;
    private final int statusCode;

    public StatusResponse(String msg, int statusCode) {
        this.msg = msg;
        this.statusCode = statusCode;
    }
}
