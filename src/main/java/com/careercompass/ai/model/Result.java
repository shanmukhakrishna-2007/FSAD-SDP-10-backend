package com.careercompass.ai.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "results")
public class Result {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assessment_id", nullable = false)
    private Assessment assessment;

    private int analyticalScore;
    private int creativeScore;
    private int technicalScore;
    private int socialScore;
    private int totalScore;

    @Column(columnDefinition = "TEXT")
    private String aiRecommendation;

    private LocalDateTime completedAt;

    @PrePersist
    protected void onComplete() { this.completedAt = LocalDateTime.now(); }

    public Result() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
    public Assessment getAssessment() { return assessment; }
    public void setAssessment(Assessment assessment) { this.assessment = assessment; }
    public int getAnalyticalScore() { return analyticalScore; }
    public void setAnalyticalScore(int s) { this.analyticalScore = s; }
    public int getCreativeScore() { return creativeScore; }
    public void setCreativeScore(int s) { this.creativeScore = s; }
    public int getTechnicalScore() { return technicalScore; }
    public void setTechnicalScore(int s) { this.technicalScore = s; }
    public int getSocialScore() { return socialScore; }
    public void setSocialScore(int s) { this.socialScore = s; }
    public int getTotalScore() { return totalScore; }
    public void setTotalScore(int s) { this.totalScore = s; }
    public String getAiRecommendation() { return aiRecommendation; }
    public void setAiRecommendation(String r) { this.aiRecommendation = r; }
    public LocalDateTime getCompletedAt() { return completedAt; }
}
