package com.blindauction.blindauctionshopproject.repository;

import com.blindauction.blindauctionshopproject.entity.SellerPermission;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SellerPermissionRepository extends JpaRepository<SellerPermission, Long> {

    List<SellerPermission> findAllByOrderByIdDesc(); // ◀ 이친구 수정필요

    Optional<SellerPermission> findById(Long userId);
}
