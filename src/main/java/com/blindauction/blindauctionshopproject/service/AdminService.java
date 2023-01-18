package com.blindauction.blindauctionshopproject.service;

import com.blindauction.blindauctionshopproject.dto.admin.SellerDetailResponse;
import com.blindauction.blindauctionshopproject.dto.admin.UserResponse;
import com.blindauction.blindauctionshopproject.entity.User;
import com.blindauction.blindauctionshopproject.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AdminService {

    private UserRepository userRepository;

    public List<UserResponse> getUserList(User user) {

        user = userRepository.findByUsername(user.getUsername()).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 아이디입니다.")
        );

        UserResponse userResponse = new UserResponse(user.getUsername(), user.getNickname());
        List<UserResponse> userResponseList = new ArrayList<>();

        userResponseList.add(userResponse);

        return userResponseList; // 페이징처리.. 어떻게?
    }
//
//    public List<SellerDetailResponse> getSellerList(User user) {
//
//        User seller = userRepository.findByUsernameAndRole(user.getUsername(),user.getRole()).orElseThrow(
//                () -> new IllegalArgumentException("존재하지 않는 아이디입니다.")
//        );
//    }

}
