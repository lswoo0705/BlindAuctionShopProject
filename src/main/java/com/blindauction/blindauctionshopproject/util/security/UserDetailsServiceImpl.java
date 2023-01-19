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

    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException { // 레퍼지토리에서 username 을 통해 유저를 가져온다
        //1. userRepository 에서 username 을 찾아
        if (userRepository.findByUsername(username).isPresent()) {
            //1-2. userRepository 에 있으면, User user 객체를 생성해서 userDetailsImpl 에 넣어서 반납해
            User user = userRepository.findByUsername(username).orElseThrow(
                    () -> new IllegalArgumentException("해당 username의 일반유저가 존재하지 않습니다")
            );
            return new UserDetailsImpl(user);
        }
        //2. 1 에 없는경우, adminRepository 에서 username 을 찾아
        Admin admin = adminRepository.findByUsername(username).orElseThrow(
                () -> new IllegalArgumentException("해당 username의 관리자가 존재하지 않습니다") //3. 둘다 없는경우 예외처리
        );
        //2-1. adminRepository 에 있는경우 admin 객체를 생성해서 userDetailsImpl 에 담아서 반납해
        return new AdminUserDetailsImpl(admin);

    }


}

