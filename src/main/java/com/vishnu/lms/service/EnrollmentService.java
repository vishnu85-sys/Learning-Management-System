package com.vishnu.lms.service;

import com.vishnu.lms.enrollment.Enrollment;
import com.vishnu.lms.enrollment.EnrollmentRepository;
import com.vishnu.lms.exception.BadRequestException;
import com.vishnu.lms.user.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EnrollmentService {
    private final EnrollmentRepository repository;
    private final CourseService courseService;

    public EnrollmentService(EnrollmentRepository repository, CourseService courseService) {
        this.repository = repository;
        this.courseService = courseService;
    }

    public Enrollment enroll(Long courseId, User student) {
        var course = courseService.findById(courseId);
        if (repository.existsByStudentIdAndCourseId(student.getId(), courseId)) {
            throw new BadRequestException("You are already enrolled in this course");
        }
        return repository.save(new Enrollment(student, course));
    }

    public List<Enrollment> mine(User student) {
        return repository.findByStudentId(student.getId());
    }
}
