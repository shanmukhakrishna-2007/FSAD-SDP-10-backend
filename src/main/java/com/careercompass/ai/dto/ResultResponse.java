package com.careercompass.ai.dto;

import java.time.LocalDateTime;

public class ResultResponse {
    private Long id;
    private int analyticalScore, creativeScore, technicalScore, socialScore, totalScore;
    private String aiRecommendation;
    private LocalDateTime date;

    public ResultResponse() {}
    public ResultResponse(Long id, int a, int c, int t, int s, int total, String ai, LocalDateTime date) {
        this.id = id; this.analyticalScore = a; this.creativeScore = c; this.technicalScore = t;
        this.socialScore = s; this.totalScore = total; this.aiRecommendation = ai; this.date = date;
    }

    public Long getId() { return id; } public void setId(Long id) { this.id = id; }
    public int getAnalyticalScore() { return analyticalScore; } public void setAnalyticalScore(int v) { this.analyticalScore = v; }
    public int getCreativeScore() { return creativeScore; } public void setCreativeScore(int v) { this.creativeScore = v; }
    public int getTechnicalScore() { return technicalScore; } public void setTechnicalScore(int v) { this.technicalScore = v; }
    public int getSocialScore() { return socialScore; } public void setSocialScore(int v) { this.socialScore = v; }
    public int getTotalScore() { return totalScore; } public void setTotalScore(int v) { this.totalScore = v; }
    public String getAiRecommendation() { return aiRecommendation; } public void setAiRecommendation(String v) { this.aiRecommendation = v; }
    public LocalDateTime getDate() { return date; } public void setDate(LocalDateTime v) { this.date = v; }

    public static ResultResponseBuilder builder() { return new ResultResponseBuilder(); }
    public static class ResultResponseBuilder {
        private Long id; private int analyticalScore, creativeScore, technicalScore, socialScore, totalScore;
        private String aiRecommendation; private LocalDateTime date;
        public ResultResponseBuilder id(Long v) { this.id = v; return this; }
        public ResultResponseBuilder analyticalScore(int v) { this.analyticalScore = v; return this; }
        public ResultResponseBuilder creativeScore(int v) { this.creativeScore = v; return this; }
        public ResultResponseBuilder technicalScore(int v) { this.technicalScore = v; return this; }
        public ResultResponseBuilder socialScore(int v) { this.socialScore = v; return this; }
        public ResultResponseBuilder totalScore(int v) { this.totalScore = v; return this; }
        public ResultResponseBuilder aiRecommendation(String v) { this.aiRecommendation = v; return this; }
        public ResultResponseBuilder date(LocalDateTime v) { this.date = v; return this; }
        public ResultResponse build() { return new ResultResponse(id, analyticalScore, creativeScore, technicalScore, socialScore, totalScore, aiRecommendation, date); }
    }
}
