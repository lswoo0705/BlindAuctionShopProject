//package com.blindauction.blindauctionshopproject.controller;
//
//import com.blindauction.blindauctionshopproject.dto.user.UserSignupRequest;
//import com.blindauction.blindauctionshopproject.service.UserService;
//import com.google.gson.Gson;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.http.MediaType;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.ResultActions;
//import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
//import org.springframework.test.web.servlet.setup.MockMvcBuilders;
//
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.when;
//
//@ExtendWith(MockitoExtension.class)
//class UserControllerTest {
//    @Mock
//    private UserService userService;
//    @InjectMocks
//    private UserController userController;
//
//    private MockMvc mockMvc;
//
//    @BeforeEach
//    public void init() {
//        // mockMvc 객체 초기화
//        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
//    }
//
//    @Test
//    @DisplayName("유저 회원가입")
//    void signupUser() throws Exception {
//    // given
//        UserSignupRequest request = UserSignupRequest.builder()
//                .username("user1")
//                .nickname("유저1")
//                .password("qweQWE123!@#")
//                .build();
//
//        when(userService.signupUser())
//
//    // when
//        ResultActions resultActions = mockMvc.perform(
//                MockMvcRequestBuilders.post("/users/signup")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(new Gson().toJson(request))
//            // Gson 의존성 추가 -> testImplementation 'com.google.code.gson:gson:2.9.0'
//        );
//
//    // then
//    }
//
//    @Test
//    void loginUser() {
//    }
//}