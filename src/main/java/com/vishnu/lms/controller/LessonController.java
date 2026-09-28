package com.vishnu.lms.controller;

import com.vishnu.lms.lesson.Lesson;
import com.vishnu.lms.service.LessonService;
import com.vishnu.lms.user.User;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/courses/{courseId}/lessons")
public class LessonController {
    private final LessonService service;

    public LessonController(LessonService service) { this.service = service; }

    @GetMapping
    public List<Lesson> list(@PathVariable Long courseId) { return service.list(courseId); }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','INSTRUCTOR')")
    public Lesson create(@PathVariable Long courseId, @Valid @RequestBody LessonRequest request,
                         Authentication authentication) {
        return service.create(courseId, request.title(), request.content(), request.videoUrl(), request.lessonOrder(),
                (User) authentication.getPrincipal());
    }

    public record LessonRequest(@NotBlank String title, String content, String videoUrl,
                                @NotNull Integer lessonOrder) {}
}
