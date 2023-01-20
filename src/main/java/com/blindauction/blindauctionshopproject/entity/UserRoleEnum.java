package com.blindauction.blindauctionshopproject.entity;

public enum UserRoleEnum {
    USER(Authority.USER),
    SELLER(Authority.SELLER),
    ADMIN(Authority.ADMIN);


    // 스프링 세큐리티에서 역할 정보 가져오기
    private final String authority;

    UserRoleEnum(String authority){
        this.authority = authority;
    }
    public String getAuthority() {
        return this.authority;
    }



    public static class Authority{
        public static final String USER = "ROLE_USER";
        public static final String SELLER = "ROLE_SELLER";
        public static final String ADMIN = "ROLE_ADMIN";
    }
}
