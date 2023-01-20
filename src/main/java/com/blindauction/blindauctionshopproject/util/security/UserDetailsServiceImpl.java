package com.blindauction.blindauctionshopproject.util.security;

import com.blindauction.blindauctionshopproject.entity.Admin;
import com.blindauction.blindauctionshopproject.entity.User;
import com.blindauction.blindauctionshopproject.repository.AdminRepository;
import com.blindauction.blindauctionshopproject.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;
    private final AdminRepository adminRepository;
// 제네릭 타입<>으로 받으면 변수에따라 갈라쓸수있다.
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException { // 레퍼지토리에서 username 을 통해 유저를 가져온다
        //1. username 으로 userRepository에서 user 찾아. 없으면 예외처리
            User user = userRepository.findByUsername(username).orElseThrow(
                    () -> new IllegalArgumentException("해당 username의 일반유저가 존재하지 않습니다")
            );
            return new UserDetailsImpl(user); // userDetailsImpl 반납
        }
    // 관맂자인경우
    public AdminUserDetailsImpl loadAdminUserByUsername(String username) throws UsernameNotFoundException{
        Admin admin = adminRepository.findByUsername(username).orElseThrow(
                () -> new IllegalArgumentException("해당 username의 관리자가 존재하지 않습니다.")
        );
        return new AdminUserDetailsImpl(admin);
    }


}

