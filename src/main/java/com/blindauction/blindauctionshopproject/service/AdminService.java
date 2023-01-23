package com.blindauction.blindauctionshopproject.service;


import com.blindauction.blindauctionshopproject.dto.admin.*;

import com.blindauction.blindauctionshopproject.dto.security.UsernameAndRoleResponse;
import com.blindauction.blindauctionshopproject.dto.admin.SellerDetailResponse;
import com.blindauction.blindauctionshopproject.dto.admin.SellerPermissionResponse;
import com.blindauction.blindauctionshopproject.dto.admin.UserResponse;
import com.blindauction.blindauctionshopproject.entity.*;

import com.blindauction.blindauctionshopproject.repository.SellerPermissionRepository;
import com.blindauction.blindauctionshopproject.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;

import static com.blindauction.blindauctionshopproject.entity.UserRoleEnum.SELLER;
import static com.blindauction.blindauctionshopproject.entity.UserRoleEnum.USER;

@Service
@RequiredArgsConstructor
public class AdminService {
    private final UserRepository userRepository;
    private final SellerPermissionRepository sellerPermissionRepository;
    private final PasswordEncoder passwordEncoder;
    private static final String ADMIN_TOKEN = "eyJzdWIiOiJoZWxsb3dvcmxkIiwibm";

    @Transactional
    public void signupAdmin(AdminSignupRequest adminSignupRequest) {
        String username = adminSignupRequest.getUsername();
        String nickname = adminSignupRequest.getNickname();
        String password = passwordEncoder.encode(adminSignupRequest.getPassword());

        Optional<User> found = userRepository.findByUsername(username);
        if (found.isPresent()) {
            throw new IllegalArgumentException("중복된 관리자 아이디가 존재합니다.");
        }
        if (!adminSignupRequest.getAdminToken().equals(ADMIN_TOKEN)) {
            throw new IllegalArgumentException("관리자 토큰이 일치하지 않습니다");
        }
        UserRoleEnum role = UserRoleEnum.ADMIN;

        User user = new User(username, nickname, password, role);
        userRepository.save(user);
    }

    @Transactional // 회원목록 조회
    public Page<UserResponse> getUserList() {
        return userRepository.findAllByRole(USER, PageRequest.of(0, 10)).map(UserResponse::new);
        // (클래스 :: 메서드)
    }

    @Transactional
    public Page<SellerDetailResponse> getSellerList() { // 판매자목록 조회
        return userRepository.findAllByRole(SELLER, PageRequest.of(0, 10))
                .map(user -> new SellerDetailResponse(user.getUsername(), user.getNickname(), user.getPhoneNum(), user.getSellerDetail()));
    }

    @Transactional
    public Page<SellerPermissionResponse> getSellerPermissionList() { // 판매자권한 요청목록 조회
        return sellerPermissionRepository.findAllByOrderByModifiedAtDesc(PageRequest.of(0, 10)).map(SellerPermissionResponse::new);
    }

    @Transactional
    public void acceptSellerRole(Long userId) { // 판매자 권한 승인
        //1. userid 에 해당하는 유저가 존재하는지
        User user = userRepository.findById(userId).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 UserId 입니다.")
        );
        //2. 존재한다면 그 유저의 역할을 확인. user의 권한이 USER인 경우, SELLER 로 전환
        if (user.isUser()) {
                user.updateUserToSeller();
            }
        if (user.isSeller()){
            throw new IllegalArgumentException("해당 유저는 이미 SELLER 입니다");
        }
        else {
            throw new IllegalArgumentException("해당 유저는 관리자 입니다");
        }


    }

    @Transactional
    public void deleteSellerRole(Long userId) { // 판매자 권한 삭제
        User seller = userRepository.findById(userId).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 userid 입니다")
        );
        if(!seller.isSeller()){
            throw new IllegalArgumentException("해당 유저는 SELLER가 아닙니다");
        }
        seller.updateSellerToUser();


    }

    //관리자 로그인
    @Transactional
    public UsernameAndRoleResponse loginAdmin(AdminLoginRequest adminLoginRequest) {
        String username = adminLoginRequest.getUsername();
        String password = adminLoginRequest.getPassword();

        User user = userRepository.findByUsername(username).orElseThrow(
                () -> new IllegalArgumentException("해당 id 는 존재하지 않습니다.")
        );
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다");
        }
        return new UsernameAndRoleResponse(user.getUsername(), user.getRole());
    }

    //1. 특정 판매자 권한 요청을 조회
    @Transactional
    public SellerPermissionResponse getSellerPermissionDetail(Long sellerPermissionId) {
        SellerPermission sellerPermission = sellerPermissionRepository.findById(sellerPermissionId).orElseThrow(
                () -> new IllegalArgumentException("해당 id 값의 sellerPermission 이 존재하지 않습니다.")
        );
        return new SellerPermissionResponse(sellerPermission);

    }
    //2. 그 특정 판매자 권한 요청을 볼때 그걸 ACCEPT & REFUSAL 하는 기능
    @Transactional
    public String AnswerSellerPermissionDetail(AnswerSellerPermissionRequest request, Long sellerPermissionId) {
        //1. sellerPermission 가져오기
        SellerPermission sellerPermission = sellerPermissionRepository.findById(sellerPermissionId).orElseThrow(
                () -> new IllegalArgumentException("해당 id 값의 sellerPermission 이 존재하지 않습니다.")
        );
        //2. sellerPermission 의 상태가 WATING 이 아닌경우 "이미 처리된 요청입니다"
        if (!sellerPermission.checkAcceptanceStatusIsWaiting()) {
            throw new IllegalArgumentException("이미 처리된 요청입니다");
        }
        //3. WAITING 인 경우 ACCEPT / REFUSAL 로 상태 변환
        //3-1 accept : sellerPermission 상태변환 + 유저를 셀러로 + permission 에서 가져온 폰번호로 유저 정보 변경
        if (request.getAnswer().equals(PermissionStatusEnum.ACCEPTANCE)) {
            sellerPermission.changeStatusAccept();
            User user = sellerPermission.getUser();
            String phoneNum = sellerPermission.getPhoneNum();
            user.updateUserToSeller();
            user.updateUserPhoneNum(phoneNum);
            return "판매자 전환요청 [수락] 처리 완료";
        }
        //3-2 Refusal : sellerPermission 상태 변환  ( 나중에 유저가 refusal 인 신청서 수정하는 기능 있음 좋겠다 )
        else if (request.getAnswer().equals(PermissionStatusEnum.REFUSAL)) {
            sellerPermission.changeStatusRefusal();
            return "판매자 전환요청 [거부] 처리 완료";
        } else throw new IllegalArgumentException("요청 처리는 ACCEPTANCE / REFUSAL 만 입력 가능합니다");
    }
}

