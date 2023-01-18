package com.blindauction.blindauctionshopproject.repository;

import com.blindauction.blindauctionshopproject.entity.User;
import com.blindauction.blindauctionshopproject.entity.UserRoleEnum;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    List<User> findAllByRole(UserRoleEnum userRoleEnum);

    Optional<User> findByIdAndRole(Long id, UserRoleEnum userRoleEnum);

    Optional<User> findByNickname(String nickname);

    Optional<User> findByUsername(String username);
}