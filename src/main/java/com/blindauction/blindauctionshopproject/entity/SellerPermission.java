package com.blindauction.blindauctionshopproject.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity(name="SELLER_PERMISSION")
public class SellerPermission extends TimeStamped{
    @Id
    private long id; // 판매글 id

    @JoinColumn(name = "nickname", nullable = false)  // 연관관계 다시 확인
    @ManyToOne
    private User user; // 셀러가 되고싶은 회원
    private String phoneNum; // 연락처
    private String permissionDetail; // 판매자 신청서 상세 내용

    public SellerPermission(String phoneNum, String permissionDetail) {
        this.phoneNum = phoneNum;
        this.permissionDetail = permissionDetail;
    }

}
