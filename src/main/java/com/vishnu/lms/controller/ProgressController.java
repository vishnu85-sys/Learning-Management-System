package com.vishnu.lms.controller;

import com.vishnu.lms.progress.Progress;
import com.vishnu.lms.service.ProgressService;
import com.vishnu.lms.user.User;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/progress")
public class ProgressController {
    private final ProgressService service;

    public ProgressController(ProgressService service) { this.service = service; }

    @PostMapping("/lessons/{lessonId}/complete")
    @PreAuthorize("hasRole('STUDENT')")
    public Progress complete(@PathVariable Long lessonId, Authentication authentication) {
        return service.complete(lessonId, (User) authentication.getPrincipal());
    }

    @GetMapping("/courses/{courseId}")
    @PreAuthorize("hasRole('STUDENT')")
    public List<Progress> courseProgress(@PathVariable Long courseId, Authentication authentication) {
        return service.forCourse(courseId, (User) authentication.getPrincipal());
    }
}
