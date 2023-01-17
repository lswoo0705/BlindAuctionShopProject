package com.blindauction.blindauctionshopproject.repository;

import com.blindauction.blindauctionshopproject.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
