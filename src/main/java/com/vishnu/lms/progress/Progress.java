package com.vishnu.lms.progress;

import com.vishnu.lms.enrollment.Enrollment;
import com.vishnu.lms.lesson.Lesson;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "progress", uniqueConstraints = @UniqueConstraint(columnNames = {"enrollment_id", "lesson_id"}))
public class Progress {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "enrollment_id", nullable = false)
    private Enrollment enrollment;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "lesson_id", nullable = false)
    private Lesson lesson;

    @Column(nullable = false)
    private boolean completed;

    @Column(nullable = false)
    private LocalDateTime completedAt;

    public Progress() {}

    public Progress(Enrollment enrollment, Lesson lesson) {
        this.enrollment = enrollment;
        this.lesson = lesson;
        this.completed = false;
    }

    public Long getId() { return id; }
    public Enrollment getEnrollment() { return enrollment; }
    public Lesson getLesson() { return lesson; }
    public boolean isCompleted() { return completed; }
    public LocalDateTime getCompletedAt() { return completedAt; }
    public void complete() {
        this.completed = true;
        this.completedAt = LocalDateTime.now();
    }
}
