package com.vishnu.lms.service;

import com.vishnu.lms.course.Course;
import com.vishnu.lms.lesson.Lesson;
import com.vishnu.lms.lesson.LessonRepository;
import com.vishnu.lms.user.Role;
import com.vishnu.lms.user.User;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LessonService {
    private final LessonRepository lessonRepository;
    private final CourseService courseService;

    public LessonService(LessonRepository lessonRepository, CourseService courseService) {
        this.lessonRepository = lessonRepository;
        this.courseService = courseService;
    }

    public List<Lesson> list(Long courseId) {
        courseService.findById(courseId);
        return lessonRepository.findByCourseIdOrderByLessonOrder(courseId);
    }

    public Lesson create(Long courseId, String title, String content, String videoUrl, Integer lessonOrder, User user) {
        Course course = courseService.findById(courseId);
        if (user.getRole() != Role.ADMIN && !course.getInstructor().getId().equals(user.getId())) {
            throw new AccessDeniedException("Only the course instructor or admin can add lessons");
        }
        return lessonRepository.save(new Lesson(title, content, videoUrl, lessonOrder, course));
    }
}
