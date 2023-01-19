package com.blindauction.blindauctionshopproject.service;

import com.blindauction.blindauctionshopproject.dto.user.SellerResponse;
import com.blindauction.blindauctionshopproject.dto.user.UserProfileResponse;
import com.blindauction.blindauctionshopproject.dto.user.UserProfileUpdateRequest;
import com.blindauction.blindauctionshopproject.entity.SellerPermission;
import com.blindauction.blindauctionshopproject.entity.User;
import com.blindauction.blindauctionshopproject.entity.UserRoleEnum;
import com.blindauction.blindauctionshopproject.repository.SellerPermissionRepository;
import com.blindauction.blindauctionshopproject.dto.user.UserSignupRequest;
import com.blindauction.blindauctionshopproject.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
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
    public void registerSellerPermission(String phoneNum, String permissionDetail) {
        SellerPermission sellerPermission = new SellerPermission(phoneNum, permissionDetail);
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

    public List<SellerResponse> getSellerList() {

        UserRoleEnum role = SELLER;

        List<User> sellerList = userRepository.findAllByRole(role);
        List<SellerResponse> sellerResponseList = new ArrayList<>();

        for(long i = 0L; i < sellerList.size(); i++) {
            User seller = userRepository.findByIdAndRole(i, role).orElseThrow(
                    () -> new IllegalArgumentException("존재하지 않는 판매자입니다.")
            );

            SellerResponse sellerResponse = new SellerResponse(
                    seller.getUsername(),
                    seller.getNickname(),
                    seller.getSellerDetail());
            sellerResponseList.add(sellerResponse);
        }

        return sellerResponseList;
    }

    public SellerResponse getSellerById(Long userId) {

        User seller = userRepository.findByIdAndRole(userId, SELLER).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 판매자입니다.")
        );

        return new SellerResponse(
                seller.getUsername(),
                seller.getNickname(),
                seller.getSellerDetail());
    }
}
