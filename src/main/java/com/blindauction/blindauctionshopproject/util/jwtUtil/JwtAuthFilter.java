package com.blindauction.blindauctionshopproject.util.jwtUtil;

import com.blindauction.blindauctionshopproject.dto.security.SecurityExceptionDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {
    private final JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String token = jwtUtil.resolveToken(request); // 토큰 값 추출

        if(token != null) { //토큰이 비어있지 않은 경우
            if(!jwtUtil.validateToken(token)){ //토큰 값이 유효하지 않은 경우
                jwtExceptionHandler(response, "토큰값이 유효하지 않습니다", HttpStatus.UNAUTHORIZED.value());
                return;
            }
            Claims info = jwtUtil.getUserInfoFromToken(token); // 토큰에 담긴 유저 정보값 저장
            setAuthentication(info.getSubject()); //Subject : username으로 들어가고 있음
        }
        filterChain.doFilter(request,response);
    }

    // 인증 정보
    public void setAuthentication(String username) {
        SecurityContext context = SecurityContextHolder.createEmptyContext(); // SecurityContext가 Authentication 을 가지고 있고 관리함. 그 SC 를 SecurityContextHolder 가 들고있음.
        Authentication authentication = jwtUtil.createAuthentication(username); // 인증된 사용자 정보를 담고있는 Authentication
        context.setAuthentication(authentication);

        SecurityContextHolder.setContext(context);
    }

        //JWT 예외 처리
        public void jwtExceptionHandler(HttpServletResponse response, String msg, int statusCode) {
        response.setStatus(statusCode);
        response.setContentType("application/json");
        try {
            String json = new ObjectMapper().writeValueAsString(new SecurityExceptionDto(statusCode, msg));
            response.getWriter().write(json);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }


}
