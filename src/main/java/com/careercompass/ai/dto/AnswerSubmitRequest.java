package com.careercompass.ai.dto;

public class AnswerSubmitRequest {
    private Long questionId;
    private int score;
    public AnswerSubmitRequest() {}
    public Long getQuestionId() { return questionId; }
    public void setQuestionId(Long questionId) { this.questionId = questionId; }
    public int getScore() { return score; }
    public void setScore(int score) { this.score = score; }
}
