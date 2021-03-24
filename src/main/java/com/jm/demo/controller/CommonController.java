package com.jm.demo.controller;

import com.jm.demo.config.JwtTokenProvider;
import com.jm.demo.repository.MemberRepository;
import com.jm.demo.vo.*;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequiredArgsConstructor
public class CommonController {
    private final JwtTokenProvider jwtTokenProvider;
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;


    @PostMapping("/login")
    @ApiOperation(value="로그인", notes = "성공시 jwt 토큰을 헤더에 넣어서 반환합니다.")
    @ApiResponses({
            @ApiResponse(code = 200, message = "로그인 성공", response = EmptyResponse.class),
            @ApiResponse(code = 400, message = "잘못된 비밀번호입니다.", response = ErrorResponse.class)
    })
    public ResponseEntity<? extends BasicResponse> login(
            @RequestBody LoginVO user,
            HttpServletResponse res
    ) {
        User member = memberRepository.findById(user.getId()).orElseThrow(() -> new IllegalArgumentException("가입되지 않은 아이디입니다."));
        if(!passwordEncoder.matches(user.getPassword(), member.getPassword())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse("잘못된 비밀번호입니다."));
        }else {
            res.setHeader(JwtTokenProvider.ACCESS_TOKEN_NAME, jwtTokenProvider.createAccessToken(member.getUsername(), member.getAuthority()));
            String refreshToken = jwtTokenProvider.createRefreshToken(member.getUsername(), member.getAuthority());
            res.setHeader(JwtTokenProvider.REFRESH_TOKEN_NAME, refreshToken);
            member.setRefreshToken(refreshToken);
            memberRepository.save(member);
        }
        return ResponseEntity.status(HttpStatus.OK).body(new EmptyResponse("성공"));
    }

    @PostMapping("/token")
    @ApiOperation(value="토큰 갱신", notes = "성공시 갱신된 jwt 토큰을 헤더에 넣어서 반환합니다.")
    @ApiResponses({
            @ApiResponse(code = 200, message = "토큰 갱신 성공", response = EmptyResponse.class)
    })
    public ResponseEntity<? extends BasicResponse> token(
            HttpServletRequest req,
            HttpServletResponse res
    ) {
        String refreshToken = req.getHeader(JwtTokenProvider.REFRESH_TOKEN_NAME);
        Authentication authentication = jwtTokenProvider.getAuthentication(refreshToken);
        User user = memberRepository.findById(authentication.getName()).orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));
        res.setHeader(JwtTokenProvider.ACCESS_TOKEN_NAME, jwtTokenProvider.createAccessToken(user.getUsername(), user.getAuthority()));
        return ResponseEntity.status(HttpStatus.OK).body(new EmptyResponse("성공"));
    }
}
