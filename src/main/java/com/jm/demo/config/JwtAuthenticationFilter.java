package com.jm.demo.config;

import com.jm.demo.repository.MemberRepository;
import com.jm.demo.vo.User;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JwtAuthenticationFilter extends BasicAuthenticationFilter {

    private final JwtTokenProvider jwtTokenProvider;
    private final MemberRepository memberRepository;

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager, JwtTokenProvider jwtTokenProvider, MemberRepository memberRepository) {
        super(authenticationManager);
        this.jwtTokenProvider = jwtTokenProvider;
        this.memberRepository = memberRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        // 헤더에서 JWT 받아옴
        String accessToken = jwtTokenProvider.getToken(request, JwtTokenProvider.ACCESS_TOKEN_NAME);
        String refreshToken = jwtTokenProvider.getToken(request, JwtTokenProvider.REFRESH_TOKEN_NAME);
        // 유효한 토큰인지 확인
        if (accessToken != null && jwtTokenProvider.validateToken(accessToken)) {
            // 토큰이 유효하면 토큰으로부터 유저 정보를 받아옴
            Authentication authentication = jwtTokenProvider.getAuthentication(accessToken);
            // SecurityContext 에 Authentication 객체를 저장
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }else {
            if(refreshToken != null) {
                Authentication authentication = jwtTokenProvider.getAuthentication(refreshToken);
                User user = memberRepository.findById(authentication.getName()).orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));
                if(user.getRefreshToken().equals(refreshToken) && jwtTokenProvider.validateToken(refreshToken)) {
                    // refresh 토큰이 유효 할 경우 SecurityContext 에 Authentication 객체를 저장
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
        }
        chain.doFilter(request, response);
    }
}
