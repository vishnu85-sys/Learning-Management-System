package com.vishnu.lms.controller;

import com.vishnu.lms.enrollment.Enrollment;
import com.vishnu.lms.service.EnrollmentService;
import com.vishnu.lms.user.User;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/enrollments")
public class EnrollmentController {
    private final EnrollmentService service;

    public EnrollmentController(EnrollmentService service) { this.service = service; }

    @PostMapping("/courses/{courseId}")
    @PreAuthorize("hasRole('STUDENT')")
    public Enrollment enroll(@PathVariable Long courseId, Authentication authentication) {
        return service.enroll(courseId, (User) authentication.getPrincipal());
    }

    @GetMapping("/me")
    @PreAuthorize("hasRole('STUDENT')")
    public List<Enrollment> mine(Authentication authentication) {
        return service.mine((User) authentication.getPrincipal());
    }
}
