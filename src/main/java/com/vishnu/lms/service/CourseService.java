package com.vishnu.lms.service;

import com.vishnu.lms.course.Course;
import com.vishnu.lms.course.CourseRepository;
import com.vishnu.lms.exception.ResourceNotFoundException;
import com.vishnu.lms.enrollment.EnrollmentRepository;
import com.vishnu.lms.lesson.LessonRepository;
import com.vishnu.lms.progress.ProgressRepository;
import com.vishnu.lms.user.Role;
import com.vishnu.lms.user.User;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CourseService {
    private final CourseRepository repository;
    private final EnrollmentRepository enrollmentRepository;
    private final LessonRepository lessonRepository;
    private final ProgressRepository progressRepository;

    public CourseService(CourseRepository repository, EnrollmentRepository enrollmentRepository,
                         LessonRepository lessonRepository, ProgressRepository progressRepository) {
        this.repository = repository;
        this.enrollmentRepository = enrollmentRepository;
        this.lessonRepository = lessonRepository;
        this.progressRepository = progressRepository;
    }

    public List<Course> findAll(String query) {
        if (query == null || query.isBlank()) return repository.findAll();
        String normalized = query.trim();
        return repository.findByTitleContainingIgnoreCaseOrCategoryContainingIgnoreCase(normalized, normalized);
    }

    public List<Course> findAll() { return repository.findAll(); }

    public Course findById(Long id) {
        return repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Course not found"));
    }

    public Course create(String title, String description, String category, User instructor) {
        return repository.save(new Course(title, description, category, instructor));
    }

    public Course update(Long id, String title, String description, String category, User user) {
        Course course = findById(id);
        requireOwnerOrAdmin(course, user);
        course.setTitle(title);
        course.setDescription(description);
        course.setCategory(category);
        return repository.save(course);
    }

    @Transactional
    public void delete(Long id, User user) {
        Course course = findById(id);
        requireOwnerOrAdmin(course, user);
        progressRepository.deleteByEnrollmentCourseId(id);
        progressRepository.deleteByLessonCourseId(id);
        enrollmentRepository.deleteByCourseId(id);
        lessonRepository.deleteByCourseId(id);
        repository.delete(course);
    }

    private void requireOwnerOrAdmin(Course course, User user) {
        if (user.getRole() != Role.ADMIN && !course.getInstructor().getId().equals(user.getId())) {
            throw new AccessDeniedException("Only the course instructor or admin can modify it");
        }
    }
}
