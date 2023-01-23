package com.blindauction.blindauctionshopproject.repository;

import com.blindauction.blindauctionshopproject.entity.LogoutToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LogoutTokenRepository extends JpaRepository<LogoutToken, Long> {
    boolean existsByToken(String token);
}
