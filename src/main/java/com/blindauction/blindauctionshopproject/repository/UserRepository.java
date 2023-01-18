package com.blindauction.blindauctionshopproject.repository;

import com.blindauction.blindauctionshopproject.entity.User;
import com.blindauction.blindauctionshopproject.entity.UserRoleEnum;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);

    Optional<User> findByUsernameAndRole(String username, UserRoleEnum userRoleEnum);

    Optional<User> findByNickname(String nickname);
}
