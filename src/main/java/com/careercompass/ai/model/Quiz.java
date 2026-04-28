package com.careercompass.ai.model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "quizzes")
public class Quiz {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(name = "company_name")
    private String companyName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "career_path_id")
    private CareerPath careerPath;

    @OneToMany(mappedBy = "quiz", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<QuizQuestion> questions = new ArrayList<>();

    private String difficulty;

    public Quiz() {}
    public Quiz(Long id, String title, String companyName, CareerPath careerPath, List<QuizQuestion> questions, String difficulty) {
        this.id = id; this.title = title; this.companyName = companyName;
        this.careerPath = careerPath; this.questions = questions; this.difficulty = difficulty;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getCompanyName() { return companyName; }
    public void setCompanyName(String companyName) { this.companyName = companyName; }
    public CareerPath getCareerPath() { return careerPath; }
    public void setCareerPath(CareerPath careerPath) { this.careerPath = careerPath; }
    public List<QuizQuestion> getQuestions() { return questions; }
    public void setQuestions(List<QuizQuestion> questions) { this.questions = questions; }
    public String getDifficulty() { return difficulty; }
    public void setDifficulty(String difficulty) { this.difficulty = difficulty; }

    public static QuizBuilder builder() { return new QuizBuilder(); }
    public static class QuizBuilder {
        private String title; private String companyName; private CareerPath careerPath;
        private List<QuizQuestion> questions = new ArrayList<>(); private String difficulty;
        public QuizBuilder title(String t) { this.title = t; return this; }
        public QuizBuilder companyName(String c) { this.companyName = c; return this; }
        public QuizBuilder careerPath(CareerPath c) { this.careerPath = c; return this; }
        public QuizBuilder questions(List<QuizQuestion> q) { this.questions = q; return this; }
        public QuizBuilder difficulty(String d) { this.difficulty = d; return this; }
        public Quiz build() {
            Quiz q = new Quiz();
            q.title = title; q.companyName = companyName; q.careerPath = careerPath;
            q.questions = questions; q.difficulty = difficulty;
            return q;
        }
    }
}
