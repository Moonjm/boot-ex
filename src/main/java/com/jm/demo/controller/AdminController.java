package com.jm.demo.controller;

import com.jm.demo.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;


@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

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

        return ret;
    }
}
