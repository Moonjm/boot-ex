package com.jm.demo.controller;

import com.jm.demo.config.JwtTokenProvider;
import com.jm.demo.repository.MemberRepository;
import com.jm.demo.vo.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class CommonController {
    private final JwtTokenProvider jwtTokenProvider;
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public Map<String, String> login(
            @RequestBody Map<String, String> user,
            HttpServletResponse res
    ) {
        Map<String, String> ret = new HashMap<>();
        User member = memberRepository.findById(user.get("id")).orElseThrow(() -> new IllegalArgumentException("가입되지 않은 아이디입니다."));
        if(!passwordEncoder.matches(user.get("password"), member.getPassword())) {
            throw new IllegalArgumentException("잘못된 비밀번호입니다.");
        }else {
            res.setHeader(JwtTokenProvider.ACCESS_TOKEN_NAME, jwtTokenProvider.createAccessToken(member.getUsername(), member.getAuthority()));
            String refreshToken = jwtTokenProvider.createRefreshToken(member.getUsername(), member.getAuthority());
            res.setHeader(JwtTokenProvider.REFRESH_TOKEN_NAME, refreshToken);
            member.setRefreshToken(refreshToken);
            memberRepository.save(member);
        }
        ret.put("result", "success");
        return ret;
    }

    @PostMapping("/token")
    public Map<String, String> token(
            HttpServletRequest req,
            HttpServletResponse res
    ) {
        Map<String, String> ret = new HashMap<>();
        String refreshToken = req.getHeader(JwtTokenProvider.REFRESH_TOKEN_NAME);
        Authentication authentication = jwtTokenProvider.getAuthentication(refreshToken);
        User user = memberRepository.findById(authentication.getName()).orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));
        res.setHeader(JwtTokenProvider.ACCESS_TOKEN_NAME, jwtTokenProvider.createAccessToken(user.getUsername(), user.getAuthority()));
        ret.put("result", "success");
        return ret;
    }
}
