package com.blindauction.blindauctionshopproject.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class StatusResponse {
    private final int statusCode;
    private final String message;
}
