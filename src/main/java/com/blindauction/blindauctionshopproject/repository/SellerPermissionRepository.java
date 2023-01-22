package com.blindauction.blindauctionshopproject.repository;

import com.blindauction.blindauctionshopproject.entity.SellerPermission;
import com.blindauction.blindauctionshopproject.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface SellerPermissionRepository extends JpaRepository<SellerPermission, Long> {

    Page<SellerPermission> findAllByOrderByModifiedAtDesc(Pageable pageable);

    @Query("select s from SELLER_PERMISSION  s where s.user=:user")
    Optional<SellerPermission> findByUser(@Param("user")User user);
}
