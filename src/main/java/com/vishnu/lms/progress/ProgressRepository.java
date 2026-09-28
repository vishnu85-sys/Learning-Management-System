package com.vishnu.lms.progress;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProgressRepository extends JpaRepository<Progress, Long> {
    Optional<Progress> findByEnrollmentIdAndLessonId(Long enrollmentId, Long lessonId);
    long countByEnrollmentIdAndCompletedTrue(Long enrollmentId);
    List<Progress> findByEnrollmentId(Long enrollmentId);

    @Modifying
    @Query("delete from Progress p where p.enrollment.course.id = :courseId")
    int deleteByEnrollmentCourseId(@Param("courseId") Long courseId);

    @Modifying
    @Query("delete from Progress p where p.lesson.course.id = :courseId")
    int deleteByLessonCourseId(@Param("courseId") Long courseId);
}
