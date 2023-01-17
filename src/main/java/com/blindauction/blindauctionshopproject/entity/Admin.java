package com.blindauction.blindauctionshopproject.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;

@Getter
@NoArgsConstructor
@Entity(name="ADMIN")
public class Admin {
        @Id
        private long id;
        private String username;
        private String nickname;
        private String password;
        private UserRoleEnum role;

    }
