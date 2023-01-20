package com.blindauction.blindauctionshopproject.repository;

import com.blindauction.blindauctionshopproject.entity.User;
import com.blindauction.blindauctionshopproject.entity.UserRoleEnum;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByIdAndRole(Long id, UserRoleEnum userRoleEnum);

    Optional<User> findByNickname(String nickname);

    Optional<User> findByUsername(String username);

    Page<User> findAllByRole(UserRoleEnum userRoleEnum, Pageable pageable);
}