package com.blindauction.blindauctionshopproject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class BlindauctionshopprojectApplication {

    public static void main(String[] args) {
        SpringApplication.run(BlindauctionshopprojectApplication.class, args);
    }

}
