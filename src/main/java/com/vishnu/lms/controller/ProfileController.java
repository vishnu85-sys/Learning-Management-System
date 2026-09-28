package com.vishnu.lms.controller;

import com.vishnu.lms.user.User;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/me")
public class ProfileController {
    @GetMapping
    public Map<String, Object> me(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return Map.of(
                "id", user.getId(),
                "name", user.getName(),
                "email", user.getEmail(),
                "role", user.getRole()
        );
    }
}
