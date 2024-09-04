package com.board.backend.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable() // CSRF protection 비활성화
                .authorizeRequests()
                .antMatchers("/users/register", "/users/login").permitAll() // 등록 및 로그인 API는 인증 없이 접근 가능
                .anyRequest().authenticated() // 다른 모든 요청은 인증 필요
                .and()
                .formLogin() // 폼 로그인 설정
                .loginPage("/login") // 로그인 페이지 설정
                .permitAll()
                .and()
                .logout() // 로그아웃 설정
                .permitAll();

        return http.build();
    }
}