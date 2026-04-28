package com.careercompass.ai.dto;

import java.util.List;

public class UserProfileResponse {
    private Long id;
    private String name;
    private String email;
    private String role;
    private Integer xp;
    private Integer level;
    private Integer problemsSolved;
    private Integer coursesCompleted;
    private Integer certificatesEarned;
    private List<String> recentBadges;

    public UserProfileResponse() {}
    public UserProfileResponse(Long id, String name, String email, String role, Integer xp, Integer level, Integer problemsSolved, Integer coursesCompleted, Integer certificatesEarned, List<String> recentBadges) {
        this.id = id; this.name = name; this.email = email; this.role = role; this.xp = xp;
        this.level = level; this.problemsSolved = problemsSolved; this.coursesCompleted = coursesCompleted;
        this.certificatesEarned = certificatesEarned; this.recentBadges = recentBadges;
    }

    public Long getId() { return id; } public void setId(Long id) { this.id = id; }
    public String getName() { return name; } public void setName(String n) { this.name = n; }
    public String getEmail() { return email; } public void setEmail(String e) { this.email = e; }
    public String getRole() { return role; } public void setRole(String r) { this.role = r; }
    public Integer getXp() { return xp; } public void setXp(Integer x) { this.xp = x; }
    public Integer getLevel() { return level; } public void setLevel(Integer l) { this.level = l; }
    public Integer getProblemsSolved() { return problemsSolved; } public void setProblemsSolved(Integer p) { this.problemsSolved = p; }
    public Integer getCoursesCompleted() { return coursesCompleted; } public void setCoursesCompleted(Integer c) { this.coursesCompleted = c; }
    public Integer getCertificatesEarned() { return certificatesEarned; } public void setCertificatesEarned(Integer c) { this.certificatesEarned = c; }
    public List<String> getRecentBadges() { return recentBadges; } public void setRecentBadges(List<String> r) { this.recentBadges = r; }

    public static UserProfileResponseBuilder builder() { return new UserProfileResponseBuilder(); }
    public static class UserProfileResponseBuilder {
        private Long id; private String name, email, role; private Integer xp, level, problemsSolved, coursesCompleted, certificatesEarned;
        private List<String> recentBadges;
        public UserProfileResponseBuilder id(Long v) { this.id = v; return this; }
        public UserProfileResponseBuilder name(String v) { this.name = v; return this; }
        public UserProfileResponseBuilder email(String v) { this.email = v; return this; }
        public UserProfileResponseBuilder role(String v) { this.role = v; return this; }
        public UserProfileResponseBuilder xp(Integer v) { this.xp = v; return this; }
        public UserProfileResponseBuilder level(Integer v) { this.level = v; return this; }
        public UserProfileResponseBuilder problemsSolved(Integer v) { this.problemsSolved = v; return this; }
        public UserProfileResponseBuilder coursesCompleted(Integer v) { this.coursesCompleted = v; return this; }
        public UserProfileResponseBuilder certificatesEarned(Integer v) { this.certificatesEarned = v; return this; }
        public UserProfileResponseBuilder recentBadges(List<String> v) { this.recentBadges = v; return this; }
        public UserProfileResponse build() { return new UserProfileResponse(id, name, email, role, xp, level, problemsSolved, coursesCompleted, certificatesEarned, recentBadges); }
    }
}
