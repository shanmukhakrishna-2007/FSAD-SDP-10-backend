package com.careercompass.ai.dto;

import java.time.LocalDateTime;
import java.util.List;

/**
 * DTOs used exclusively by Admin CRUD endpoints.
 * Centralised in one file to keep the DTO package clean.
 */
public final class AdminDTOs {

    private AdminDTOs() {} // utility class

    // ── Course ──

    public static class CreateCourseRequest {
        private String title;
        private String description;
        private Long careerPathId;
        private String difficulty;
        private int durationHours;
        private String topics;
        private boolean trending;

        public CreateCourseRequest() {}

        public String getTitle() { return title; }
        public void setTitle(String title) { this.title = title; }
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
        public Long getCareerPathId() { return careerPathId; }
        public void setCareerPathId(Long careerPathId) { this.careerPathId = careerPathId; }
        public String getDifficulty() { return difficulty; }
        public void setDifficulty(String difficulty) { this.difficulty = difficulty; }
        public int getDurationHours() { return durationHours; }
        public void setDurationHours(int durationHours) { this.durationHours = durationHours; }
        public String getTopics() { return topics; }
        public void setTopics(String topics) { this.topics = topics; }
        public boolean isTrending() { return trending; }
        public void setTrending(boolean trending) { this.trending = trending; }
    }

    // ── Problem ──

    public static class TestCaseDTO {
        private String input;
        private String expectedOutput;
        private boolean hidden;

        public TestCaseDTO() {}

        public String getInput() { return input; }
        public void setInput(String input) { this.input = input; }
        public String getExpectedOutput() { return expectedOutput; }
        public void setExpectedOutput(String expectedOutput) { this.expectedOutput = expectedOutput; }
        public boolean isHidden() { return hidden; }
        public void setHidden(boolean hidden) { this.hidden = hidden; }
    }

    public static class CreateProblemRequest {
        private String title;
        private String description;
        private String difficulty; // EASY, MEDIUM, HARD
        private String tags;
        private String baseCode;
        private Integer xpReward;
        private List<TestCaseDTO> testCases;

        public CreateProblemRequest() {}

        public String getTitle() { return title; }
        public void setTitle(String title) { this.title = title; }
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
        public String getDifficulty() { return difficulty; }
        public void setDifficulty(String difficulty) { this.difficulty = difficulty; }
        public String getTags() { return tags; }
        public void setTags(String tags) { this.tags = tags; }
        public String getBaseCode() { return baseCode; }
        public void setBaseCode(String baseCode) { this.baseCode = baseCode; }
        public Integer getXpReward() { return xpReward; }
        public void setXpReward(Integer xpReward) { this.xpReward = xpReward; }
        public List<TestCaseDTO> getTestCases() { return testCases; }
        public void setTestCases(List<TestCaseDTO> testCases) { this.testCases = testCases; }
    }

    // ── Contest ──

    public static class CreateContestRequest {
        private String name;
        private LocalDateTime startTime;
        private LocalDateTime endTime;
        private String difficulty; // EASY, MEDIUM, HARD
        private List<Long> problemIds;
        private boolean weekly;

        public CreateContestRequest() {}

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public LocalDateTime getStartTime() { return startTime; }
        public void setStartTime(LocalDateTime startTime) { this.startTime = startTime; }
        public LocalDateTime getEndTime() { return endTime; }
        public void setEndTime(LocalDateTime endTime) { this.endTime = endTime; }
        public String getDifficulty() { return difficulty; }
        public void setDifficulty(String difficulty) { this.difficulty = difficulty; }
        public List<Long> getProblemIds() { return problemIds; }
        public void setProblemIds(List<Long> problemIds) { this.problemIds = problemIds; }
        public boolean isWeekly() { return weekly; }
        public void setWeekly(boolean weekly) { this.weekly = weekly; }
    }
}
