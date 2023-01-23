package com.blindauction.blindauctionshopproject;

import com.blindauction.blindauctionshopproject.entity.*;
import com.blindauction.blindauctionshopproject.repository.ProductRepository;
import com.blindauction.blindauctionshopproject.repository.PurchasePermissionRepository;
import com.blindauction.blindauctionshopproject.repository.SellerPermissionRepository;
import com.blindauction.blindauctionshopproject.repository.UserRepository;
import com.blindauction.blindauctionshopproject.service.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.crypto.password.PasswordEncoder;

@EnableJpaAuditing
@SpringBootApplication
public class BlindauctionshopprojectApplication {

    public static void main(String[] args) {
        SpringApplication.run(BlindauctionshopprojectApplication.class, args);
    }

    @Bean
    public CommandLineRunner dummyData(ProductRepository productRepository,
                                       PurchasePermissionRepository purchasePermissionRepository,
                                       SellerPermissionRepository sellerPermissionRepository,
                                       UserRepository userRepository,
                                       PasswordEncoder passwordEncoder,
                                       UserService userService) {
        return (args -> {
            // 관리자, 판매자, 유저 생성
            User admin = new User("admin", "관리자", passwordEncoder.encode("qweQWE123!@#"), UserRoleEnum.ADMIN);
            User seller = new User("seller1", "판매자", passwordEncoder.encode("qweQWE123!@#"), UserRoleEnum.SELLER);
            User user = new User("user1", "일반유저", passwordEncoder.encode("qweQWE123!@#"), UserRoleEnum.USER);

            // 생성한 유저들 레퍼지토리에 저장
            userRepository.save(admin);
            userRepository.save(seller);
            userRepository.save(user);

            // seller 가 작성한 판매글 생성 & 레퍼지토리 저장
            Product product = new Product(seller, "인형 팝니다", (long)30000, "춘식이 인형 팝니다");
            productRepository.save(product);

            // user 가 작성한 거래요청글 생성 & 레퍼지토리 저장
            purchasePermissionRepository.save(new PurchasePermission(product, user, "춘식인형 너무 갖고싶어요", (long)35000, TransactionStatusEnum.WAITING));
            product.plusBidderCnt();
        });
    }

}
