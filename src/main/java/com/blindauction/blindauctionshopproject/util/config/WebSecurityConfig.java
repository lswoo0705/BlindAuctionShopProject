package com.blindauction.blindauctionshopproject.util.config;

import com.blindauction.blindauctionshopproject.repository.LogoutTokenRepository;
import com.blindauction.blindauctionshopproject.util.jwtUtil.JwtAuthFilter;
import com.blindauction.blindauctionshopproject.util.jwtUtil.JwtUtil;
import com.blindauction.blindauctionshopproject.util.security.CustomAccessDeniedHandler;
import com.blindauction.blindauctionshopproject.util.security.CustomAuthenticationEntryPoint;
import com.blindauction.blindauctionshopproject.util.security.UserDetailsServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity  // 스프링 세큐리티 기능 지원
@EnableGlobalMethodSecurity(securedEnabled = true) // @Secured 어노테이션 활성화
public class WebSecurityConfig {
    private final JwtUtil jwtUtil; // jwt를 사용하기 위해서 객체 생성
    private final UserDetailsServiceImpl userDetailsService;

    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;

    private final CustomAccessDeniedHandler customAccessDeniedHandler;

    private final LogoutTokenRepository logoutTokenRepository;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean //WebSecurity 는 패턴에 해당하는 리소스에 아예 SpringSecurity 를 적용하지 않게 설정 ( = 자유롭게 access )
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring()
                .requestMatchers(PathRequest.toH2Console())
                .requestMatchers(PathRequest.toStaticResources().atCommonLocations());

    }

    @Bean // HttpSecurity 는 WebSecurity 와 다르게, 리소스 이외의 부분에는 Spring Security 를 설정하도록 함
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf().disable(); //cross site request forgery protection: get을 제외한 상태를 변화시킬 수 있는 post put delete 요청을 막아줌
        //disable 하는 이유 : session 기반과 달리 rest api를 이용한 서버는 sateless 하기 때문에 서버에 인증정보를 보관하지 않음.
        //서버가 인증과 관련된 정보를 저장하지 않으므로 csrf 코드를 사용할 필요가 없음.
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS); // security의 기본 session 방식을 사용하지 않고 JWT 방식을 사용하기 위한 설정

        //이하 인가 없이 접속할 수 있는 페이지(permitAll)를 정의
        http.authorizeRequests().antMatchers("/users/signup").permitAll()
                .antMatchers("/users/login").permitAll()
                .antMatchers("/admin/signup").permitAll()
                .antMatchers("/admin/login").permitAll()

                .antMatchers("/admin/**").hasAnyRole("ADMIN")

                .antMatchers("/users/**").hasAnyRole("USER", "SELLER")
                .antMatchers("/sellers/**").hasAnyRole("SELLER")

                .antMatchers("/product/**").hasAnyRole("USER")

                .anyRequest().authenticated()
                //이하 jwt 를 인증&인가에 사용하기 위한 설정임.
                .and().addFilterBefore(new JwtAuthFilter(jwtUtil, userDetailsService, logoutTokenRepository), UsernamePasswordAuthenticationFilter.class);

        http.exceptionHandling().authenticationEntryPoint(customAuthenticationEntryPoint); //401

        http.exceptionHandling().accessDeniedHandler(customAccessDeniedHandler); //403

        return http.build();
    }


}

