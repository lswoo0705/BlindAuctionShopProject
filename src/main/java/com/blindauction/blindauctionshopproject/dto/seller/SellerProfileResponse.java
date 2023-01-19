package com.blindauction.blindauctionshopproject.dto.seller;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SellerProfileResponse {
    private final String username;
    private final String nickname;
    private final String sellerDetail;
}
