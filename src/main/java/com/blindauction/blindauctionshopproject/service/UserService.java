package com.blindauction.blindauctionshopproject.service;

import com.blindauction.blindauctionshopproject.dto.security.UsernameAndRoleResponse;
import com.blindauction.blindauctionshopproject.dto.user.UserLoginRequest;
import com.blindauction.blindauctionshopproject.dto.user.SellerResponse;
import com.blindauction.blindauctionshopproject.dto.user.UserProfileResponse;
import com.blindauction.blindauctionshopproject.entity.SellerPermission;
import com.blindauction.blindauctionshopproject.entity.User;
import com.blindauction.blindauctionshopproject.entity.UserRoleEnum;
import com.blindauction.blindauctionshopproject.repository.SellerPermissionRepository;
import com.blindauction.blindauctionshopproject.dto.user.UserSignupRequest;
import com.blindauction.blindauctionshopproject.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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
    public void updateUserProfile(String nickname, String password, String userInfo) {
        User user = userRepository.findByUsername(userInfo).orElseThrow(
                () -> new IllegalArgumentException("해당 유저를 찾을 수 없습니다.")
        );

        if (user.getUsername().equals(userInfo)) {
            if (passwordEncoder.matches(password, user.getPassword())) {
                user.updateUserProfile(nickname);
            } throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        } throw new IllegalArgumentException("본인의 프로필만 설정할 수 있습니다.");
    }

    // 판매자 등록 요청
    public void registerSellerPermission(String phoneNum, String permissionDetail, String username) {
//        //1. username 의 유저가 있는지 생성하고
//        SellerPermission sellerPermission = new SellerPermission(phoneNum, permissionDetail);
//        sellerPermissionRepository.save(sellerPermission);

        //2. SellerPermission객체를 생성후 레퍼지토리 넣기 (1. phonnum, permissiondetail,
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
        return userRepository.findAllByRole(SELLER, PageRequest.of(10, 10))
                .map(user -> new SellerResponse(user.getUsername(), user.getNickname(), user.getSellerDetail()));
    }
}
