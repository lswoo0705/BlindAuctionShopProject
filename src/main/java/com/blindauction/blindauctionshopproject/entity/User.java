package com.blindauction.blindauctionshopproject.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import static com.blindauction.blindauctionshopproject.entity.UserRoleEnum.SELLER;
import static com.blindauction.blindauctionshopproject.entity.UserRoleEnum.USER;

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
    // 나의 프로필 설정(수정)
    public void updateUserProfile(String nickname) {
        this.nickname = nickname;
    }

    public User(String username, String nickname, String password, String phoneNum, UserRoleEnum role) {
        this.username = username;
        this.nickname = nickname;
        this.password = password;
        this.phoneNum = phoneNum;
        this.role = role;
    }

    public User(String username, String nickname, String password, String phoneNum, String sellerDetail, UserRoleEnum role) {
        this.username = username;
        this.nickname = nickname;
        this.password = password;
        this.phoneNum = phoneNum;
        this.sellerDetail = sellerDetail;
        this.role = role;
    }

    public boolean isSeller(){ //User 가 Seller 인지 확인해주는 메소드
        return this.getRole().equals(SELLER);
    }
    public boolean isUser(){
        return this.getRole().equals(USER);
    }

    public void updateSellerProfile(String nickname, String sellerDetail){
        this.nickname = nickname;
        this.sellerDetail = sellerDetail;
    }

    public void updateUserToSeller() {
        this.role = SELLER;
    }

    public void updateSellerToUser() {
        this.role = USER;
    }
    public void updateUserPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }
}