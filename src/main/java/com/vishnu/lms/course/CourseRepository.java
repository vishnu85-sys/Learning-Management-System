package com.vishnu.lms.course;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CourseRepository extends JpaRepository<Course, Long> {
    List<Course> findByTitleContainingIgnoreCaseOrCategoryContainingIgnoreCase(String title, String category);
    Optional<Course> findByTitleIgnoreCase(String title);
}
