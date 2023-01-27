package com.blindauction.blindauctionshopproject.service;

import com.blindauction.blindauctionshopproject.dto.security.UsernameAndRoleResponse;
import com.blindauction.blindauctionshopproject.dto.user.UserLoginRequest;
import com.blindauction.blindauctionshopproject.dto.user.UserProfileResponse;
import com.blindauction.blindauctionshopproject.dto.user.UserSignupRequest;
import com.blindauction.blindauctionshopproject.entity.User;
import com.blindauction.blindauctionshopproject.entity.UserRoleEnum;
import com.blindauction.blindauctionshopproject.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockBean(JpaMetamodelMappingContext.class)
class UserServiceTest {
    @Mock
    private UserRepository userRepository;
    //    @Mock 옳지 못해!
//    private User user;
    @InjectMocks
    private UserService userService;

    // 아래 둘 중에 하나 사용해야됨
//    @Mock
//    private PasswordEncoder passwordEncoder;
    @Mock
    private PasswordEncoder passwordEncoder;

    @Test
    @DisplayName("유저 회원가입")
    void signupUser() {
    // given
        UserSignupRequest request = UserSignupRequest.builder()
                .username("user1")
                .nickname("유저1")
                .password("qweQWE123!@#")
                .build();

        when(userRepository.findByUsername(any(String.class)))
                .thenReturn(Optional.empty());

        when(userRepository.findByNickname(any(String.class)))
                .thenReturn(Optional.empty());

    // when
        userService.signupUser(request);


    // then
        verify(userRepository, times(1)).save(any(User.class));

    }

    @Test
    @DisplayName("유저 로그인")
    void loginUser() {
    // given
        UserLoginRequest request = UserLoginRequest.builder()
                .username("user1")
                .password("qweQWE123!@#")
                .build();

        // 임의의 유저 생성
        User user = new User("user1", "유저1", passwordEncoder.encode("qweQWE123!@#"), UserRoleEnum.USER);
        when(userRepository.findByUsername(any(String.class)))
                .thenReturn(Optional.of(user));

    // when
        UsernameAndRoleResponse response = userService.loginUser(request);

    // then
        assertThat(response.getUsername()).isEqualTo("user1");
        assertThat(response.getRole()).isEqualTo(UserRoleEnum.USER);

    }

    @Test
    @DisplayName("나의 프로필 조회")
    void getUserProfile() {
    // given
        User user = new User("user1", "유저1", passwordEncoder.encode("qweQWE123!@#"), UserRoleEnum.USER);
        when(userRepository.findByUsername(any(String.class)))
                .thenReturn(Optional.of(user));

    // when
        UserProfileResponse response = userService.getUserProfile(user.getUsername());

    // then
        assertThat(response.getUsername()).isEqualTo("user1");
        assertThat(response.getNickname()).isEqualTo("유저1");

    }

    @Test
    @DisplayName("나의 프로필 설정")
    void updateUserProfile() {
//    // given
//        // request에서 받아온 값
//        String nickname = "변경유저1"; // -> 이거로 변경할거
//        String password = "qweQWE123!@#"; // -> 확인용
//
//        // 객체 user1
//        // 여기서 Mock 초기화됨
//        User user1 = new User("user1", "유저1", passwordEncoder.encode("qweQWE123!@#"), UserRoleEnum.USER);
//        User user = mock(User.class);
//
//        when(userRepository.findByUsername(any(String.class)))
//                .thenReturn(Optional.of(user1));
//
//
//        doNothing().when(user).updateUserProfile(nickname);
//
////        when(user1.updateUserProfile(any(String.class)));
////        doReturn(any()).when(user1).updateUserProfile(nickname);
////        doNothing().when(user).updateUserProfile(nickname);
//
//    // when
//        userService.updateUserProfile(nickname, password, user1.getUsername());
//
//    // then  에러발생?!
//        verify(user, times(1)).updateUserProfile("외않되");
////        assertThat(user1.getNickname()).isEqualTo(nickname);

//        String nickname = "nickname";
//        String password = "password";
        String userInfo = "userInfo";
//        User user = User.builder().nickname(nickname).password(password).username(userInfo).build();
        User mockUser = mock(User.class);
//        when(user.getUsername()).thenReturn(userInfo);
        when(mockUser.getUsername()).thenReturn("asdasd");

        when(userRepository.findByUsername(any(String.class))).thenReturn(Optional.of(mockUser));
//        when(mockUser.getUsername().equals("asdasd")).thenReturn(true);
        when(passwordEncoder.matches(any(String.class),any(String.class))).thenReturn(true);

        userService.updateUserProfile(any(String.class),any(String.class),(userInfo));

        verify(mockUser).updateUserProfile(any(String.class));
    }
}