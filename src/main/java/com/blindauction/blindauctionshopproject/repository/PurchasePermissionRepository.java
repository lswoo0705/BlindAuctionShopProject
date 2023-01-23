package com.blindauction.blindauctionshopproject.repository;

import com.blindauction.blindauctionshopproject.entity.Product;
import com.blindauction.blindauctionshopproject.entity.PurchasePermission;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PurchasePermissionRepository extends JpaRepository<PurchasePermission, Long> {
    PurchasePermission findByProduct(Product product);
}