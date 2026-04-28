package com.careercompass.ai.model;

import jakarta.persistence.*;

@Entity
@Table(name = "problems")
public class Problem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    private Difficulty difficulty;

    private String tags;

    @Column(columnDefinition = "TEXT")
    private String baseCode;

    private Integer xpReward;

    public Problem() {}
    public Problem(Long id, String title, String description, Difficulty difficulty, String tags, String baseCode, Integer xpReward) {
        this.id = id; this.title = title; this.description = description; this.difficulty = difficulty;
        this.tags = tags; this.baseCode = baseCode; this.xpReward = xpReward;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public Difficulty getDifficulty() { return difficulty; }
    public void setDifficulty(Difficulty difficulty) { this.difficulty = difficulty; }
    public String getTags() { return tags; }
    public void setTags(String tags) { this.tags = tags; }
    public String getBaseCode() { return baseCode; }
    public void setBaseCode(String baseCode) { this.baseCode = baseCode; }
    public Integer getXpReward() { return xpReward; }
    public void setXpReward(Integer xpReward) { this.xpReward = xpReward; }

    public static ProblemBuilder builder() { return new ProblemBuilder(); }
    public static class ProblemBuilder {
        private String title; private String description; private Difficulty difficulty;
        private String tags; private String baseCode; private Integer xpReward;
        public ProblemBuilder title(String t) { this.title = t; return this; }
        public ProblemBuilder description(String d) { this.description = d; return this; }
        public ProblemBuilder difficulty(Difficulty d) { this.difficulty = d; return this; }
        public ProblemBuilder tags(String t) { this.tags = t; return this; }
        public ProblemBuilder baseCode(String b) { this.baseCode = b; return this; }
        public ProblemBuilder xpReward(Integer x) { this.xpReward = x; return this; }
        public Problem build() {
            Problem p = new Problem();
            p.title = title; p.description = description; p.difficulty = difficulty;
            p.tags = tags; p.baseCode = baseCode; p.xpReward = xpReward;
            return p;
        }
    }
}
