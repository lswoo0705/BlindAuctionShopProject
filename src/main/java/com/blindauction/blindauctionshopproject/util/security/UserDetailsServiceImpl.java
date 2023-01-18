package com.blindauction.blindauctionshopproject.util.security;

import com.blindauction.blindauctionshopproject.entity.User;
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

    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException{ // 레퍼지토리에서 username 을 통해 유저를 가져온다
        User user = userRepository.findByUsername(username).orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다"));

        return new UserDetailsImpl(user);

    }
}
