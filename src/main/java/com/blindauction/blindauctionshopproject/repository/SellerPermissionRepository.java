package com.blindauction.blindauctionshopproject.repository;

import com.blindauction.blindauctionshopproject.entity.SellerPermission;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SellerPermissionRepository extends JpaRepository<SellerPermission, Long> {

    List<SellerPermission> findAllDesc();
}
