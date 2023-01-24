package com.blindauction.blindauctionshopproject.util.security;

import com.blindauction.blindauctionshopproject.dto.security.SecurityExceptionDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException {
//        response.sendError(HttpStatus.UNAUTHORIZED.value(), HttpStatus.UNAUTHORIZED.getReasonPhrase());
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        try {
            String json = new ObjectMapper().writeValueAsString(new SecurityExceptionDto(HttpStatus.UNAUTHORIZED.value(), "인증에 실패했습니다"));
            response.getWriter().write(json);
        } catch (Exception e) {
            throw new IllegalArgumentException("왜 오류를 내보내지를 못하니 대체 왜");

        }
    }
}
