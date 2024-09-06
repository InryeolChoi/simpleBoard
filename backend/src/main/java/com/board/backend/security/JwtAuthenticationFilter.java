package com.board.backend.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        // 요청 헤더에서 Authorization 헤더를 가져옴
        final String authorizationHeader = request.getHeader("Authorization");

        String username = null;
        String jwt = null;

        // Authorization 헤더가 존재하고, "Bearer "로 시작하는 경우
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            jwt = authorizationHeader.substring(7); // "Bearer " 이후의 토큰 부분 추출
            username = jwtUtil.extractUsername(jwt); // JWT에서 사용자 이름 추출
        }

        // SecurityContext에 사용자가 인증되지 않은 경우 (즉, 이미 인증된 상태가 아닌 경우)
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            // UserDetailsService를 통해 사용자 정보를 가져옴
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);

            // JWT 토큰이 유효한지 검증
            if (jwtUtil.validateToken(jwt, userDetails.getUsername())) {

                // 사용자 정보를 기반으로 인증 객체 생성
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());

                // 추가 세부정보 설정
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // SecurityContext에 인증 정보를 설정
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
        }

        // 필터 체인의 다음 필터로 요청 전달
        filterChain.doFilter(request, response);
    }
}