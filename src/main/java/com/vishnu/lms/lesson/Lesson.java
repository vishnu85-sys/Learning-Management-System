package com.vishnu.lms.lesson;

import com.vishnu.lms.course.Course;
import jakarta.persistence.*;

@Entity
@Table(name = "lessons")
public class Lesson {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(length = 5000)
    private String content;

    @Column(name = "video_url")
    private String videoUrl;

    @Column(name = "lesson_order", nullable = false)
    private Integer lessonOrder;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    public Lesson() {}

    public Lesson(String title, String content, String videoUrl, Integer lessonOrder, Course course) {
        this.title = title;
        this.content = content;
        this.videoUrl = videoUrl;
        this.lessonOrder = lessonOrder;
        this.course = course;
    }

    public Long getId() { return id; }
    public String getTitle() { return title; }
    public String getContent() { return content; }
    public String getVideoUrl() { return videoUrl; }
    public Integer getLessonOrder() { return lessonOrder; }
    public Course getCourse() { return course; }

    public void setTitle(String title) { this.title = title; }
    public void setContent(String content) { this.content = content; }
    public void setVideoUrl(String videoUrl) { this.videoUrl = videoUrl; }
}
