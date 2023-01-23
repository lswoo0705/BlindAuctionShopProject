package com.blindauction.blindauctionshopproject.dto.admin;

import com.blindauction.blindauctionshopproject.entity.PermissionStatusEnum;
import lombok.Getter;

@Getter
public class AnswerSellerPermissionRequest {
    private PermissionStatusEnum answer;
}
