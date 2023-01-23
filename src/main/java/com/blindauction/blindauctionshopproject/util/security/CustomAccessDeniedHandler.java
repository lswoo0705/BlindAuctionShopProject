package com.blindauction.blindauctionshopproject.util.security;

import com.blindauction.blindauctionshopproject.dto.security.SecurityExceptionDto;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws IOException {
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        try {
            String json = new ObjectMapper().writeValueAsString(new SecurityExceptionDto(HttpStatus.FORBIDDEN.value(), "권한이 없습니다"));
            response.getWriter().write(json);
        }
        catch (Exception e) {
            throw new IllegalArgumentException("사람살려요");
        }


    }
}
