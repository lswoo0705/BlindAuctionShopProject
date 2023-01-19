package com.blindauction.blindauctionshopproject.dto.security;

import com.blindauction.blindauctionshopproject.entity.AdminRoleEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AdminUsernameAndRoleResponse {
    private String username;
    private AdminRoleEnum role;
}
