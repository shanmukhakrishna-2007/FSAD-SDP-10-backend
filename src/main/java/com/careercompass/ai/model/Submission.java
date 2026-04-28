package com.careercompass.ai.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "submissions")
public class Submission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "problem_id", nullable = false)
    private Problem problem;

    @Enumerated(EnumType.STRING)
    private SubmissionStatus status;

    @Column(columnDefinition = "TEXT")
    private String code;

    private String language;
    private Integer xpEarned;
    private LocalDateTime submittedAt;

    @PrePersist
    protected void onCreate() { this.submittedAt = LocalDateTime.now(); }

    public Submission() {}
    public Submission(Long id, User user, Problem problem, SubmissionStatus status, String code, String language, Integer xpEarned, LocalDateTime submittedAt) {
        this.id = id; this.user = user; this.problem = problem; this.status = status;
        this.code = code; this.language = language; this.xpEarned = xpEarned; this.submittedAt = submittedAt;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
    public Problem getProblem() { return problem; }
    public void setProblem(Problem problem) { this.problem = problem; }
    public SubmissionStatus getStatus() { return status; }
    public void setStatus(SubmissionStatus status) { this.status = status; }
    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }
    public String getLanguage() { return language; }
    public void setLanguage(String language) { this.language = language; }
    public Integer getXpEarned() { return xpEarned; }
    public void setXpEarned(Integer xpEarned) { this.xpEarned = xpEarned; }
    public LocalDateTime getSubmittedAt() { return submittedAt; }

    public static SubmissionBuilder builder() { return new SubmissionBuilder(); }
    public static class SubmissionBuilder {
        private User user; private Problem problem; private SubmissionStatus status;
        private String code; private String language; private Integer xpEarned;
        public SubmissionBuilder user(User user) { this.user = user; return this; }
        public SubmissionBuilder problem(Problem problem) { this.problem = problem; return this; }
        public SubmissionBuilder status(SubmissionStatus status) { this.status = status; return this; }
        public SubmissionBuilder code(String code) { this.code = code; return this; }
        public SubmissionBuilder language(String language) { this.language = language; return this; }
        public SubmissionBuilder xpEarned(Integer xpEarned) { this.xpEarned = xpEarned; return this; }
        public Submission build() {
            Submission s = new Submission();
            s.user = user; s.problem = problem; s.status = status;
            s.code = code; s.language = language; s.xpEarned = xpEarned;
            return s;
        }
    }
}
