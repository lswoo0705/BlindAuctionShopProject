package com.blindauction.blindauctionshopproject.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;

@Getter
@NoArgsConstructor
@Entity(name="ADMIN")
public class SellerPermission {
    @Id
    private long id; // 판매글 id
    private User user; // 셀러가 되고싶은 회원
    private String phoneNum; // 연락처
    private String permissionDetail; // 판매자 신청서 상세 내용


}
