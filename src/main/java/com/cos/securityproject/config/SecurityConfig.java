package com.cos.securityproject.config;

import com.cos.securityproject.config.oauth.PrincipalOauth2UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.expression.WebExpressionAuthorizationManager;

// 1. 코드받기(인증), 2. 엑세스토큰(권한), 3.사용자 프로필 정보 4-1. 자동 회원가입
// 4-2. (이메일, 전화번호, 이름, 아이디) 쇼핑몰 -> (집주소), 백화점몰 -> (회원등급)

@Configuration
@EnableWebSecurity // 지금 만드는 스프링 시큐리티 필터가 스프링 필터체인에 등록
@EnableMethodSecurity(securedEnabled = true)
public class SecurityConfig{

    @Autowired
    private PrincipalOauth2UserService principalOauth2UserService;

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authorizeHttpRequests(authroize -> authroize.requestMatchers("/user/**")
                        .authenticated()
                        .requestMatchers("/manager/**")
                        .access(new WebExpressionAuthorizationManager("hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')"))
                        .requestMatchers("/admin/**")
                        .access(new WebExpressionAuthorizationManager("hasRole('ROLE_ADMIN')"))
                        .anyRequest().permitAll())
                .formLogin().loginPage("/loginForm")
                .loginProcessingUrl("/login") // login 주소가 호출되면 시큐리티가 낚어채서 대신 로그인
                .defaultSuccessUrl("/")
                .and()
                .oauth2Login()
                .loginPage("/loginForm")
                .userInfoEndpoint()
                .userService(principalOauth2UserService); // 구글 로그인이 완료된 뒤의 후처리가 필요함, 코드x (엑세스토큰 + 사용자정보 o)

        return http.build();
    }
}
