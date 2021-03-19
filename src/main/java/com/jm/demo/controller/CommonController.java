package com.jm.demo.controller;

import com.jm.demo.config.JwtTokenProvider;
import com.jm.demo.repository.MemberRepository;
import com.jm.demo.vo.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class CommonController {
    private final JwtTokenProvider jwtTokenProvider;
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public Map<String, String> login(@RequestBody Map<String, String> user) {
        Map<String, String> ret = new HashMap<>();
        User member = memberRepository.findById(user.get("id")).orElseThrow(() -> new IllegalArgumentException("가입되지 않은 아이디입니다."));
        if(!passwordEncoder.matches(user.get("password"), member.getPassword())) {
            throw new IllegalArgumentException("잘못된 비밀번호입니다.");
        }
        ret.put("token", jwtTokenProvider.createToken(member.getUsername(), member.getAuthority()));
        return ret;
    }
}
