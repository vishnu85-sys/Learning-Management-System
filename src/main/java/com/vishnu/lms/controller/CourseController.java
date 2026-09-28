package com.vishnu.lms.controller;

import com.vishnu.lms.course.Course;
import com.vishnu.lms.service.CourseService;
import com.vishnu.lms.user.User;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("/api/courses")
public class CourseController {
    private final CourseService service;

    public CourseController(CourseService service) { this.service = service; }

    @GetMapping
    public List<Course> all(@RequestParam(required = false) String query) { return service.findAll(query); }

    @GetMapping("/{id}")
    public Course one(@PathVariable Long id) { return service.findById(id); }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','INSTRUCTOR')")
    public Course create(@Valid @RequestBody CourseRequest request, Authentication authentication) {
        return service.create(request.title(), request.description(), request.category(), (User) authentication.getPrincipal());
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','INSTRUCTOR')")
    public Course update(@PathVariable Long id, @Valid @RequestBody CourseRequest request, Authentication authentication) {
        return service.update(id, request.title(), request.description(), request.category(), (User) authentication.getPrincipal());
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','INSTRUCTOR')")
    public void delete(@PathVariable Long id, Authentication authentication) {
        service.delete(id, (User) authentication.getPrincipal());
    }

    public record CourseRequest(@NotBlank String title, @NotBlank @Size(max = 2000) String description,
                                @NotBlank String category) {}
}
