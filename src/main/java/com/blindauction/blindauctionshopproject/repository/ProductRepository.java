package com.blindauction.blindauctionshopproject.repository;

import com.blindauction.blindauctionshopproject.entity.Product;
import com.blindauction.blindauctionshopproject.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long>  {
    List<Product> findProductById(Long id);
    Page<Product> findAllBySeller(User user, PageRequest pageable);
    Page<Product> findAllBySellerUsername(String username, PageRequest pageable);
}
