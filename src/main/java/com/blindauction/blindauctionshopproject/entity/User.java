package com.blindauction.blindauctionshopproject.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity(name="USERS")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false, unique = true)
    private String nickname;

    @Column(nullable = false)
    private String password;


    @Column(nullable = true)
    private String phoneNum;

    @Column(nullable = true)
    private String sellerDetail;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private UserRoleEnum role;

// 생성자
    public User(String username, String nickname, String password, UserRoleEnum role){
        this.username = username;
        this.nickname = nickname;
        this.password = password;
        this.role = role;
    }

    public User(long id, String username, String nickname, String password, String phoneNum, UserRoleEnum role) {
        this.id = id;
        this.username = username;
        this.nickname = nickname;
        this.password = password;
        this.phoneNum = phoneNum;
        this.role = role;
    }
}