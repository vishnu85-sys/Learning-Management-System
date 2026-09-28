package com.vishnu.lms.service;

import com.vishnu.lms.enrollment.EnrollmentRepository;
import com.vishnu.lms.exception.BadRequestException;
import com.vishnu.lms.exception.ResourceNotFoundException;
import com.vishnu.lms.lesson.LessonRepository;
import com.vishnu.lms.progress.Progress;
import com.vishnu.lms.progress.ProgressRepository;
import com.vishnu.lms.user.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProgressService {
    private final ProgressRepository progressRepository;
    private final EnrollmentRepository enrollmentRepository;
    private final LessonRepository lessonRepository;

    public ProgressService(ProgressRepository progressRepository, EnrollmentRepository enrollmentRepository,
                           LessonRepository lessonRepository) {
        this.progressRepository = progressRepository;
        this.enrollmentRepository = enrollmentRepository;
        this.lessonRepository = lessonRepository;
    }

    public Progress complete(Long lessonId, User student) {
        var lesson = lessonRepository.findById(lessonId)
                .orElseThrow(() -> new ResourceNotFoundException("Lesson not found"));
        var enrollment = enrollmentRepository.findByStudentIdAndCourseId(student.getId(), lesson.getCourse().getId())
                .orElseThrow(() -> new BadRequestException("Enroll in the course first"));

        Progress progress = progressRepository.findByEnrollmentIdAndLessonId(enrollment.getId(), lessonId)
                .orElseGet(() -> new Progress(enrollment, lesson));
        progress.complete();
        Progress saved = progressRepository.save(progress);

        long total = lessonRepository.findByCourseIdOrderByLessonOrder(lesson.getCourse().getId()).size();
        long completed = progressRepository.countByEnrollmentIdAndCompletedTrue(enrollment.getId());
        enrollment.setProgressPercent(total == 0 ? 0 : (int) Math.min(100, Math.round((completed * 100.0) / total)));
        enrollmentRepository.save(enrollment);
        return saved;
    }

    public List<Progress> forCourse(Long courseId, User student) {
        var enrollment = enrollmentRepository.findByStudentIdAndCourseId(student.getId(), courseId)
                .orElseThrow(() -> new BadRequestException("Enroll in the course first"));
        return progressRepository.findByEnrollmentId(enrollment.getId());
    }
}
