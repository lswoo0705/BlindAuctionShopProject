package com.blindauction.blindauctionshopproject.repository;

import com.blindauction.blindauctionshopproject.entity.SellerPermission;
import com.blindauction.blindauctionshopproject.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SellerPermissionRepository extends JpaRepository<SellerPermission, Long> {

    Page<SellerPermission> findAllByOrderByModifiedAtDesc(Pageable pageable);

    Optional<SellerPermission> findByUser(User user);
}
