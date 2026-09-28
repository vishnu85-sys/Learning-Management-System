package com.vishnu.lms.config;

import com.vishnu.lms.course.Course;
import com.vishnu.lms.course.CourseRepository;
import com.vishnu.lms.lesson.Lesson;
import com.vishnu.lms.lesson.LessonRepository;
import com.vishnu.lms.user.Role;
import com.vishnu.lms.user.User;
import com.vishnu.lms.user.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner seed(UserRepository users,
                           CourseRepository courses,
                           LessonRepository lessons,
                           PasswordEncoder encoder) {
        return args -> {
            User admin = users.findByEmail("admin@lms.local").orElseGet(() ->
                    users.save(new User("LMS Admin", "admin@lms.local", encoder.encode("Admin@123"), Role.ADMIN)));

            User instructor = users.findByEmail("instructor@lms.local").orElseGet(() ->
                    users.save(new User("Demo Instructor", "instructor@lms.local", encoder.encode("Instructor@123"), Role.INSTRUCTOR)));

            users.findByEmail("student@lms.local").orElseGet(() ->
                    users.save(new User("Demo Student", "student@lms.local", encoder.encode("Student@123"), Role.STUDENT)));

            // Migrate the original demo Java course name when upgrading an existing LMS database.
            courses.findByTitleIgnoreCase("Java Full Stack Fundamentals").ifPresent(legacy -> {
                if (courses.findByTitleIgnoreCase("Java Programming Masterclass").isEmpty()) {
                    legacy.setTitle("Java Programming Masterclass");
                    legacy.setDescription("A structured Java course covering programming fundamentals, OOP, collections, exceptions and modern Java practices.");
                    legacy.setCategory("Java");
                    courses.save(legacy);
                }
            });

            seedCourse(courses, lessons, instructor,
                    "Java Programming Masterclass",
                    "A structured Java course covering programming fundamentals, OOP, collections, exceptions and modern Java practices.",
                    "Java",
                    List.of(
                            lesson("Java Fundamentals", "Variables, data types, operators, input and basic program structure.", "https://www.youtube.com/watch?v=A74TOX803D0", 1),
                            lesson("Control Flow and Arrays", "Conditions, loops, arrays and practical problem-solving patterns.", "https://www.youtube.com/watch?v=A74TOX803D0&t=5738s", 2),
                            lesson("Collections and HashMaps", "ArrayList, Sets, Maps and the Java Collections Framework.", "https://www.youtube.com/watch?v=A74TOX803D0&t=10442s", 3),
                            lesson("Object-Oriented Programming", "Classes, objects, constructors, inheritance, interfaces and encapsulation.", "https://www.youtube.com/watch?v=A74TOX803D0&t=12518s", 4),
                            lesson("Exceptions and Clean Java Code", "Exception handling, debugging patterns and writing maintainable Java code.", "https://www.youtube.com/watch?v=A74TOX803D0&t=14500s", 5)
                    ));

            seedCourse(courses, lessons, instructor,
                    "Python Programming Masterclass",
                    "Learn Python from fundamentals through functions, collections, object-oriented programming and practical application development.",
                    "Python",
                    List.of(
                            lesson("Python Basics", "Syntax, variables, numbers, strings and the Python execution model.", "https://www.youtube.com/watch?v=rfscVS0vtbw", 1),
                            lesson("Conditions, Loops and Functions", "Control flow, reusable functions and problem-solving with Python.", "https://www.youtube.com/watch?v=rfscVS0vtbw&t=3600s", 2),
                            lesson("Lists, Tuples, Sets and Dictionaries", "Core Python collections and when to use each data structure.", "https://www.youtube.com/watch?v=rfscVS0vtbw&t=7200s", 3),
                            lesson("Files, Modules and Error Handling", "Read and write files, work with modules and handle runtime errors.", "https://www.youtube.com/watch?v=rfscVS0vtbw&t=10800s", 4),
                            lesson("Object-Oriented Python", "Classes, objects, inheritance and building reusable Python components.", "https://www.youtube.com/watch?v=rfscVS0vtbw&t=14400s", 5)
                    ));

            seedCourse(courses, lessons, instructor,
                    "MySQL Database Masterclass",
                    "Master relational database concepts, SQL queries, joins, normalization, indexes and practical MySQL database design.",
                    "MySQL",
                    List.of(
                            lesson("Database Fundamentals", "Schemas, tables, primary keys, foreign keys and relational database concepts.", "https://www.youtube.com/watch?v=7S_tz1z_5bA", 1),
                            lesson("SELECT, INSERT, UPDATE and DELETE", "Core SQL commands for reading and modifying relational data.", "https://www.youtube.com/watch?v=7S_tz1z_5bA&t=1800s", 2),
                            lesson("Joins, GROUP BY and Subqueries", "Combine tables, aggregate data and write reusable subqueries.", "https://www.youtube.com/watch?v=7S_tz1z_5bA&t=3600s", 3),
                            lesson("Normalization and Database Design", "Design normalized tables and avoid redundancy and update anomalies.", "https://www.youtube.com/watch?v=7S_tz1z_5bA&t=5400s", 4),
                            lesson("Indexes and Production SQL", "Improve query performance and apply practical database optimization techniques.", "https://www.youtube.com/watch?v=7S_tz1z_5bA&t=7200s", 5)
                    ));

            seedCourse(courses, lessons, instructor,
                    "Spring Boot Backend Engineering",
                    "Build production-style REST APIs with Spring Boot, Spring Data JPA, validation, security and MySQL integration.",
                    "Spring Boot",
                    List.of(
                            lesson("Spring Boot Fundamentals", "Project structure, dependency injection, configuration and application startup.", "https://www.youtube.com/watch?v=9SGDpanrc8U", 1),
                            lesson("REST API Development", "Controllers, HTTP methods, request mapping, DTOs and REST API conventions.", "https://www.youtube.com/watch?v=9SGDpanrc8U&t=1200s", 2),
                            lesson("Spring Data JPA and MySQL", "Entities, repositories, relationships and persistence with MySQL.", "https://www.youtube.com/watch?v=9SGDpanrc8U&t=2400s", 3),
                            lesson("Validation and Exception Handling", "Bean validation, custom exceptions and clean API error responses.", "https://www.youtube.com/watch?v=9SGDpanrc8U&t=3600s", 4),
                            lesson("Spring Security and JWT", "Authentication, authorization, secure endpoints and JWT-based API security.", "https://www.youtube.com/watch?v=9SGDpanrc8U&t=4800s", 5)
                    ));
        };
    }

    private void seedCourse(CourseRepository courses,
                            LessonRepository lessons,
                            User instructor,
                            String title,
                            String description,
                            String category,
                            List<LessonSeed> lessonSeeds) {
        Course course = courses.findByTitleIgnoreCase(title).orElseGet(() ->
                courses.save(new Course(title, description, category, instructor)));

        for (LessonSeed seed : lessonSeeds) {
            Lesson lesson = lessons.findByCourseIdAndLessonOrder(course.getId(), seed.lessonOrder())
                    .orElseGet(() -> lessons.save(new Lesson(
                            seed.title(),
                            seed.content(),
                            seed.videoUrl(),
                            seed.lessonOrder(),
                            course)));

            lesson.setTitle(seed.title());
            lesson.setContent(seed.content());
            lesson.setVideoUrl(seed.videoUrl());
            lessons.save(lesson);
        }
    }

    private LessonSeed lesson(String title, String content, String videoUrl, int lessonOrder) {
        return new LessonSeed(title, content, videoUrl, lessonOrder);
    }

    private record LessonSeed(String title, String content, String videoUrl, int lessonOrder) {}
}
