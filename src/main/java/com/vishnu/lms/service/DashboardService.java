package com.vishnu.lms.service;

import com.vishnu.lms.course.CourseRepository;
import com.vishnu.lms.enrollment.EnrollmentRepository;
import com.vishnu.lms.user.Role;
import com.vishnu.lms.user.User;
import com.vishnu.lms.user.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class DashboardService {
    private final CourseRepository courseRepository;
    private final EnrollmentRepository enrollmentRepository;
    private final UserRepository userRepository;

    public DashboardService(CourseRepository courseRepository, EnrollmentRepository enrollmentRepository,
                            UserRepository userRepository) {
        this.courseRepository = courseRepository;
        this.enrollmentRepository = enrollmentRepository;
        this.userRepository = userRepository;
    }

    public Map<String, Object> summary(User user) {
        return Map.of(
                "user", Map.of("id", user.getId(), "name", user.getName(), "email", user.getEmail(), "role", user.getRole()),
                "courses", courseRepository.count(),
                "students", userRepository.countByRole(Role.STUDENT),
                "enrollments", enrollmentRepository.count()
        );
    }
}
