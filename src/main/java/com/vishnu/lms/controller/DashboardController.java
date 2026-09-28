package com.vishnu.lms.controller;

import com.vishnu.lms.service.DashboardService;
import com.vishnu.lms.user.User;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {
    private final DashboardService service;

    public DashboardController(DashboardService service) { this.service = service; }

    @GetMapping
    public Map<String, Object> summary(Authentication authentication) {
        return service.summary((User) authentication.getPrincipal());
    }
}
