package com.vishnu.lms.enrollment;

import com.vishnu.lms.course.Course;
import com.vishnu.lms.user.User;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "enrollments", uniqueConstraints = @UniqueConstraint(columnNames = {"student_id", "course_id"}))
public class Enrollment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "student_id", nullable = false)
    private User student;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    @Column(nullable = false)
    private LocalDateTime enrolledAt;

    @Column(nullable = false)
    private int progressPercent;

    public Enrollment() {}

    public Enrollment(User student, Course course) {
        this.student = student;
        this.course = course;
        this.enrolledAt = LocalDateTime.now();
        this.progressPercent = 0;
    }

    public Long getId() { return id; }
    public User getStudent() { return student; }
    public Course getCourse() { return course; }
    public LocalDateTime getEnrolledAt() { return enrolledAt; }
    public int getProgressPercent() { return progressPercent; }
    public void setProgressPercent(int progressPercent) { this.progressPercent = progressPercent; }
}
