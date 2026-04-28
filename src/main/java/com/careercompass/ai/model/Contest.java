package com.careercompass.ai.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "contests")
public class Contest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private LocalDateTime startTime;
    private LocalDateTime endTime;

    @Enumerated(EnumType.STRING)
    private Difficulty difficulty;

    @ManyToMany
    @JoinTable(
        name = "contest_problems",
        joinColumns = @JoinColumn(name = "contest_id"),
        inverseJoinColumns = @JoinColumn(name = "problem_id")
    )
    private List<Problem> problems;

    public Contest() {}
    public Contest(Long id, String name, LocalDateTime startTime, LocalDateTime endTime, Difficulty difficulty, List<Problem> problems) {
        this.id = id; this.name = name; this.startTime = startTime; this.endTime = endTime;
        this.difficulty = difficulty; this.problems = problems;
    }

    public static ContestBuilder builder() { return new ContestBuilder(); }
    public static class ContestBuilder {
        private Long id; private String name; private LocalDateTime startTime; private LocalDateTime endTime;
        private Difficulty difficulty; private List<Problem> problems;
        public ContestBuilder id(Long id) { this.id = id; return this; }
        public ContestBuilder name(String name) { this.name = name; return this; }
        public ContestBuilder startTime(LocalDateTime startTime) { this.startTime = startTime; return this; }
        public ContestBuilder endTime(LocalDateTime endTime) { this.endTime = endTime; return this; }
        public ContestBuilder difficulty(Difficulty difficulty) { this.difficulty = difficulty; return this; }
        public ContestBuilder problems(List<Problem> problems) { this.problems = problems; return this; }
        public Contest build() { return new Contest(id, name, startTime, endTime, difficulty, problems); }
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public LocalDateTime getStartTime() { return startTime; }
    public void setStartTime(LocalDateTime startTime) { this.startTime = startTime; }
    public LocalDateTime getEndTime() { return endTime; }
    public void setEndTime(LocalDateTime endTime) { this.endTime = endTime; }
    public Difficulty getDifficulty() { return difficulty; }
    public void setDifficulty(Difficulty difficulty) { this.difficulty = difficulty; }
    public List<Problem> getProblems() { return problems; }
    public void setProblems(List<Problem> problems) { this.problems = problems; }
}
