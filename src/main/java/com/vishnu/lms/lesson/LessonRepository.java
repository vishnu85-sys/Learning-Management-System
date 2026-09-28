package com.vishnu.lms.lesson;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface LessonRepository extends JpaRepository<Lesson, Long> {
    List<Lesson> findByCourseIdOrderByLessonOrder(Long courseId);

    java.util.Optional<Lesson> findByCourseIdAndLessonOrder(Long courseId, Integer lessonOrder);

    @Modifying
    @Query("delete from Lesson l where l.course.id = :courseId")
    int deleteByCourseId(@Param("courseId") Long courseId);
}
