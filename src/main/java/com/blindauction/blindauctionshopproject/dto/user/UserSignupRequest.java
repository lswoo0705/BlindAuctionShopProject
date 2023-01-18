package com.blindauction.blindauctionshopproject.dto.user;

import lombok.Getter;

import javax.validation.constraints.Pattern;

import javax.validation.constraints.Size;


@Getter
public class UserSignupRequest {

    @Pattern(regexp = "^[0-9a-z]*$", message = "아이디 형식에 맞지 않습니다. 아이디는 4 ~ 10자리 영문(소문자), 숫자로 이루어져야 합니다.")
    @Size(min = 4, max = 10, message = "아이디는 4글자 이상, 10글자 이하 입니다.")
    private String username;

    private String nickname;

    @Pattern(regexp = "^[0-9a-zA-Z!@#$%^&*()_=+?]*$", message = "비밀번호 형식에 맞지 않습니다. 비밀번호는 8 ~ 15자리 영문(대,소문자), 숫자, 특수문자로 이루어져야 합니다.")
    @Size(min = 8, max = 15, message = "비밀번호는 8글자 이상, 15글자 이하로 작성")
    private String password;
}
