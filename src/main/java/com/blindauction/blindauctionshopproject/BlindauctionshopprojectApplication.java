package com.blindauction.blindauctionshopproject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class BlindauctionshopprojectApplication {

    public static void main(String[] args) {
        SpringApplication.run(BlindauctionshopprojectApplication.class, args);
    }

}
