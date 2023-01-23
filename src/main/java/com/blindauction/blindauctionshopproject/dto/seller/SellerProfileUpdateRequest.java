package com.blindauction.blindauctionshopproject.dto.seller;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SellerProfileUpdateRequest {
    private final String nickname;
    private final String password;
    private final String sellerDetail;
}