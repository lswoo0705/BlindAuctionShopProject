package com.blindauction.blindauctionshopproject.controller;

//import com.blindauction.blindauctionshopproject.dto.admin.SellerDetailResponse;
import com.blindauction.blindauctionshopproject.dto.admin.UserResponse;
import com.blindauction.blindauctionshopproject.service.AdminService;
import com.blindauction.blindauctionshopproject.util.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    @GetMapping("/users")
    public List<UserResponse> getUserList(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return adminService.getUserList(userDetails.getUser());
    }
//
//    @GetMapping("/sellers")
//    public List<SellerDetailResponse> getSellerList(@AuthenticationPrincipal UserDetailsImpl userDetails) {
//        return adminService.getUserList(userDetails.getUser());
//    }

}
