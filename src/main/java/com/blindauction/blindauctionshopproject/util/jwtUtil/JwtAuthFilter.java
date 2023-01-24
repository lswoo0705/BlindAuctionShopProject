package com.blindauction.blindauctionshopproject.util.jwtUtil;

import com.blindauction.blindauctionshopproject.dto.security.SecurityExceptionDto;
import com.blindauction.blindauctionshopproject.repository.LogoutTokenRepository;
import com.blindauction.blindauctionshopproject.util.security.UserDetailsServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
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
    private final UserDetailsServiceImpl userDetailsService;

    private final LogoutTokenRepository logoutTokenRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String token = jwtUtil.resolveToken(request); // 토큰 값 추출

        if(logoutTokenRepository.existsByToken(token)){
            throw new IllegalArgumentException("이미 로그아웃 처리 된 토큰입니다");
        }

        if (token != null) { //토큰이 비어있지 않은 경우
            if (!jwtUtil.validateToken(token)) { //토큰 값이 유효하지 않은 경우
                jwtExceptionHandler(response, "토큰값이 유효하지 않습니다", HttpStatus.UNAUTHORIZED.value());
                return;
            }
            Claims info = jwtUtil.getUserInfoFromToken(token); // 토큰에 담긴 유저 정보값 저장
            setAuthentication(info.getSubject()); //Subject : username으로 들어가고 있음 (* d여기서 롤까지 가져오고싶은데....)
        } // setAuthentication 이 subject 말고도 role 도 받게끔 변경함. 근데 info.get(~) 이거 맞나?????????
        filterChain.doFilter(request, response);
    }


    private Authentication createAuthentication(String username) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }

    // 인증 정보
    public void setAuthentication(String username) {
        SecurityContext context = SecurityContextHolder.createEmptyContext(); // SecurityContext가 Authentication 을 가지고 있고 관리함. 그 SC 를 SecurityContextHolder 가 들고있음.
        Authentication authentication = this.createAuthentication(username); // 인증된 사용자 정보를 담고있는 Authentication
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
            throw new IllegalArgumentException("사람살려요");
        }
    }


}
