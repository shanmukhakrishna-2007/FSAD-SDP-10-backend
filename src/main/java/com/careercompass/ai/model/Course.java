package com.careercompass.ai.model;

import jakarta.persistence.*;

@Entity
@Table(name = "courses")
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "career_path_id")
    private CareerPath careerPath;

    private String difficulty;

    @Column(name = "duration_hours")
    private int durationHours;

    @Column(name = "topics", columnDefinition = "TEXT")
    private String topics;

    private boolean trending;

    public Course() {}
    public Course(Long id, String title, String description, CareerPath careerPath, String difficulty, int durationHours, String topics, boolean trending) {
        this.id = id; this.title = title; this.description = description; this.careerPath = careerPath;
        this.difficulty = difficulty; this.durationHours = durationHours; this.topics = topics; this.trending = trending;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public CareerPath getCareerPath() { return careerPath; }
    public void setCareerPath(CareerPath careerPath) { this.careerPath = careerPath; }
    public String getDifficulty() { return difficulty; }
    public void setDifficulty(String difficulty) { this.difficulty = difficulty; }
    public int getDurationHours() { return durationHours; }
    public void setDurationHours(int durationHours) { this.durationHours = durationHours; }
    public String getTopics() { return topics; }
    public void setTopics(String topics) { this.topics = topics; }
    public boolean isTrending() { return trending; }
    public void setTrending(boolean trending) { this.trending = trending; }

    public static CourseBuilder builder() { return new CourseBuilder(); }
    public static class CourseBuilder {
        private Long id; private String title; private String description; private CareerPath careerPath;
        private String difficulty; private int durationHours; private String topics; private boolean trending;
        public CourseBuilder id(Long i) { this.id = i; return this; }
        public CourseBuilder title(String t) { this.title = t; return this; }
        public CourseBuilder description(String d) { this.description = d; return this; }
        public CourseBuilder careerPath(CareerPath c) { this.careerPath = c; return this; }
        public CourseBuilder difficulty(String d) { this.difficulty = d; return this; }
        public CourseBuilder durationHours(int h) { this.durationHours = h; return this; }
        public CourseBuilder topics(String t) { this.topics = t; return this; }
        public CourseBuilder trending(boolean t) { this.trending = t; return this; }
        public Course build() { return new Course(id, title, description, careerPath, difficulty, durationHours, topics, trending); }
    }
}
