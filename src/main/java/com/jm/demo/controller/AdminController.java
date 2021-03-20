package com.jm.demo.controller;

import com.jm.demo.service.AdminService;
import com.jm.demo.vo.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;


@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;
    private final PasswordEncoder passwordEncoder;

    @GetMapping("/users")
    public Map<String, Object> getUsers() {
        Map<String, Object> ret = new HashMap<>();
        ret.put("users", adminService.getUsers());
        return ret;
    }

    @PostMapping("/user")
    public Map<String, Object> addUser(
            @RequestBody Map<String, String> user
    ) {
        Map<String, Object> ret = new HashMap<>();
        try {
            User userData = new User();
            userData.setId(user.get("id"));
            userData.setPassword(passwordEncoder.encode(user.get("password")));
            userData.setAuthority(user.get("authority"));
            adminService.saveUser(userData);
            ret.put("result", "성공");
        }catch (Exception e) {
            ret.put("result", "실패");
        }
        return ret;
    }
}
