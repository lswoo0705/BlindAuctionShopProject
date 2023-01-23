package com.blindauction.blindauctionshopproject.repository;

import com.blindauction.blindauctionshopproject.entity.Product;
import com.blindauction.blindauctionshopproject.entity.PurchasePermission;
import com.blindauction.blindauctionshopproject.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PurchasePermissionRepository extends JpaRepository<PurchasePermission, Long> {
    PurchasePermission findByProduct(Product product);
    List<PurchasePermission> findByProductAndBidder(Product product, User bidder);
    Page<PurchasePermission> findAllByBidder(User user, Pageable pageable);
}