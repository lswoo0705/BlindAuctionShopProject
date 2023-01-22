package com.blindauction.blindauctionshopproject.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity(name="BLACKLIST")
public class LogoutToken {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(nullable = false)
    String token;

    public LogoutToken(String token){
        this.token = token;
    }
}
