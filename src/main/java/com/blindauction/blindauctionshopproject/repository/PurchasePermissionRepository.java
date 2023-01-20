package com.blindauction.blindauctionshopproject.repository;

import com.blindauction.blindauctionshopproject.entity.PurchasePermission;
import com.blindauction.blindauctionshopproject.entity.TransactionStatusEnum;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PurchasePermissionRepository extends JpaRepository<PurchasePermission, Long> {
    List<PurchasePermission> findPurchasePermissionBy();

    List<PurchasePermission> findByTransactionStatus(TransactionStatusEnum transactionStatusEnum);
}