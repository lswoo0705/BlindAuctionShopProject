package com.blindauction.blindauctionshopproject.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity(name="SELLER_PERMISSION")
public class SellerPermission extends TimeStamped{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id; // 판매글 id

    @JoinColumn(name = "user_id", nullable = false)  // 연관관계 다시 확인
    @ManyToOne
    private User user; // 셀러가 되고싶은 회원
    private String phoneNum; // 연락처
    private String permissionDetail; // 판매자 신청서 상세 내용
    @Enumerated(value = EnumType.STRING)
    private PermissionStatusEnum acceptance_status; // 대기중 / 수락 / 거부

    public SellerPermission(User user, String phoneNum, String permissionDetail) {
        this.user = user;
        this.phoneNum = phoneNum;
        this.permissionDetail = permissionDetail;
        this.acceptance_status = PermissionStatusEnum.WAITING;
    }

    public void changeStatusAccept(){
        this.acceptance_status = PermissionStatusEnum.ACCEPTANCE;
    }

    public void changeStatusRefusal(){
        this.acceptance_status = PermissionStatusEnum.REFUSAL;
    }

    public boolean checkAcceptanceStatusIsWaiting(){
        return this.acceptance_status.equals(PermissionStatusEnum.WAITING);
    }


}
