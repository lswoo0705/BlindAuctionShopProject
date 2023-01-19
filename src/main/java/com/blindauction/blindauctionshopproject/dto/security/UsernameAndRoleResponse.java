package com.blindauction.blindauctionshopproject.dto.security;

import com.blindauction.blindauctionshopproject.entity.UserRoleEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UsernameAndRoleResponse {
    private String username;
    private UserRoleEnum role;


}
