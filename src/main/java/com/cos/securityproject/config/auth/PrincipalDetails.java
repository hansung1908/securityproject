package com.cos.securityproject.config.auth;

// 시큐리티가 /login 주소요청이 오면 낚아채서 로그인 진행
// 로그인 진행 완료되면 시큐리티 session 생성 (Security ContextHolder)
// 오브젝트 타입 -> Authentication 타입 객체
// Authentication 안에는 User정보 있어야 함
// User 오브젝트 타입 -> UserDetails 타입 객체

// Security session -> Authentication -> UserDetails(PrincipalDetails)

import com.cos.securityproject.model.User;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

@Data
public class PrincipalDetails implements UserDetails, OAuth2User {

    private User user; // 컴포시션

    private Map<String, Object> attributes;

    // 일반 로그인
    public PrincipalDetails(User user) {
        this.user = user;
    }

    // Oauth 로그인
    public PrincipalDetails(User user, Map<String, Object> attributes) {
        this.user = user;
        this.attributes = attributes;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    // 해당 User의 권한을 리턴하는 곳
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> collection = new ArrayList<>();
        collection.add(new GrantedAuthority() {
            @Override
            public String getAuthority() {
                return user.getRole();
            }
        });
        return collection;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    // 계정 만료 여부
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    // 계정 잠금 여부
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    //계정 만료기간 지났는지 여부
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    //계정 활성화 여부
    @Override
    public boolean isEnabled() {
        // 만약 사이트에서 1년동안 로그인 안 하면 휴면 계정 전환하기로 함
        // 현재시간 - 로그인시간 -> 1년 초과시 return false;
        return true;
    }

    @Override
    public String getName() {
        return null;
    }
}
