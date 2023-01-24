package com.blindauction.blindauctionshopproject.service;

import com.blindauction.blindauctionshopproject.dto.security.UsernameAndRoleResponse;
import com.blindauction.blindauctionshopproject.dto.user.*;
import com.blindauction.blindauctionshopproject.entity.*;
import com.blindauction.blindauctionshopproject.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static com.blindauction.blindauctionshopproject.entity.UserRoleEnum.SELLER;

@Service
@RequiredArgsConstructor
public class UserService {

    private final SellerPermissionRepository sellerPermissionRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final LogoutTokenRepository logoutTokenRepository;
    private final PurchasePermissionRepository purchasePermissionRepository;

    // 나의 프로필 조회
    public UserProfileResponse getUserProfile(String userInfo) {
        User user = userRepository.findByUsername(userInfo).orElseThrow(
                () -> new IllegalArgumentException("해당 유저를 찾을 수 없습니다.")
        );
        String userProfileName = user.getUsername();
        String userProfileNick = user.getNickname();

        return new UserProfileResponse(userProfileName, userProfileNick);
    }

    // 나의 프로필 설정(수정)
    @Transactional
    public void updateUserProfile(String nickname, String password, String userInfo) {
        User user = userRepository.findByUsername(userInfo).orElseThrow(
                () -> new IllegalArgumentException("해당 유저를 찾을 수 없습니다.")
        );

        if (user.getUsername().equals(userInfo)) {
            if (passwordEncoder.matches(password, user.getPassword())) {
                user.updateUserProfile(nickname);
            } else throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        } else throw new IllegalArgumentException("본인의 프로필만 설정할 수 있습니다.");
    }

    // 판매자 등록 요청
    @Transactional
    public void registerSellerPermission(String phoneNum, String permissionDetail, String username) {
        User user = userRepository.findByUsername(username).orElseThrow(
                () -> new IllegalArgumentException("해당 유저를 찾을 수 없습니다.")
        );

        // 요청 중복 방지
        if (sellerPermissionRepository.findByUser(user).isPresent()) {
            throw new IllegalArgumentException("이미 신청된 판매자 등록 요청입니다.");
        }

        SellerPermission sellerPermission = new SellerPermission(user, phoneNum, permissionDetail);
        sellerPermissionRepository.save(sellerPermission);
    }

    //유저 회원가입
    public void signupUser(UserSignupRequest userSignupRequest) {
        String username = userSignupRequest.getUsername();
        String nickname = userSignupRequest.getNickname();
        String password = passwordEncoder.encode(userSignupRequest.getPassword());

        Optional<User> foundUsername = userRepository.findByUsername(username);
        if (foundUsername.isPresent()) {
            throw new IllegalArgumentException("중복된 아이디가 존재합니다.");
        }
        Optional<User> foundNickname = userRepository.findByNickname(nickname);
        if (foundNickname.isPresent()) {
            throw new IllegalArgumentException("중복된 닉네임이 존재합니다.");
        }
        UserRoleEnum role = UserRoleEnum.USER;

        User user = new User(username, nickname, password, role);
        userRepository.save(user);
    }

    // user 로그인
    public UsernameAndRoleResponse loginUser(UserLoginRequest userLoginRequest) {
        String username = userLoginRequest.getUsername();
        String password = userLoginRequest.getPassword();

        User user = userRepository.findByUsername(username).orElseThrow(
                () -> new IllegalArgumentException("해당 id 는 존재하지 않습니다.")
        );
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다");
        }
        return new UsernameAndRoleResponse(username, user.getRole());
    }

    // 판매자 개별 조회
    @Transactional
    public SellerResponse getSellerById(Long userId) {

        User seller = userRepository.findByIdAndRole(userId, SELLER).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 판매자입니다.")
        );

        return new SellerResponse(
                seller.getUsername(),
                seller.getNickname(),
                seller.getSellerDetail());
    }

    // 판매자 목록 조회
    @Transactional
    public Page<SellerResponse> getSellerList() {
        return userRepository.findAllByRole(SELLER, PageRequest.of(0, 10))
                .map(user -> new SellerResponse(user.getUsername(), user.getNickname(), user.getSellerDetail()));
    }

    //유저 로그아웃 (블랙리스트에 추가)
    public void logoutUser(String token) {
        LogoutToken logoutToken = new LogoutToken(token);
        logoutTokenRepository.save(logoutToken);
    }

    // 나의 전체 구매신청 상태 조회
    @Transactional
    public Page<PurchaseStatusGetResponse> getPurchaseStatus(String username, int page) {
        Pageable pageable = PageRequest.of(page, 5, Sort.by(Sort.Direction.ASC, "id"));

        User user = userRepository.findByUsername(username).orElseThrow(
                () -> new IllegalArgumentException("해당 유저를 찾을 수 없습니다.")
        );

        Page<PurchasePermission> purchasePermissionPage = purchasePermissionRepository.findAllByBidder(user, pageable);

        return purchasePermissionPage.map(PurchaseStatusGetResponse::new);
    }
}
