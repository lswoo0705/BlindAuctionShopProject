package com.blindauction.blindauctionshopproject.util.security;

import com.blindauction.blindauctionshopproject.dto.security.SecurityExceptionDto;
import com.blindauction.blindauctionshopproject.dto.security.StatusResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {
    private static final StatusResponse statusResponse = new StatusResponse(HttpStatus.FORBIDDEN.value(), "접근이 거부되었습니다.");
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws IOException {
        throw new IOException("권한이 없습니다.");
    }
}
