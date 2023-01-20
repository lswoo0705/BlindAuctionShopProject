package com.blindauction.blindauctionshopproject.util.security;

import com.blindauction.blindauctionshopproject.entity.Admin;
import com.blindauction.blindauctionshopproject.entity.UserRoleEnum;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

public class AdminUserDetailsImpl implements UserDetails  {
    private final Admin admin;

    //생성자
    public AdminUserDetailsImpl(Admin admin){
        this.admin = admin;
    }

    public Admin getUser() { return admin;}

    public Collection<? extends GrantedAuthority> getAuthorities(){ // GrantedAuthority : 인증개쳉 부여된 권한. 권한 자체를 String으로 출력해주거나, AccessDecisionManager의 특별 서포트를 받음.
        // 즉, 이 메소드는 Authority를 받은 Collection에 소속된 어떤 객체든 뱉어낸다는 뜻인듯.
        UserRoleEnum role = admin.getRole(); // 유저의 역할 꺼냄
        String authority = role.getAuthority(); //

        SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority(authority); //SimpleGrantedAuthority : 인증개체에 부여된 권한의 문자열 표현을 저장
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(simpleGrantedAuthority);

        return authorities;
    }

    @Override
    public String getUsername() { // 구분자 역할을 하는 아이가 들어가는거임
        return this.admin.getUsername();
    }

    @Override
    public String getPassword() {
        return null; // 비밀번호는 주면 안되는데.. 일단 null
    }


    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }

}
