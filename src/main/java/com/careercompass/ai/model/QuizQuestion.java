package com.careercompass.ai.model;

import jakarta.persistence.*;

@Entity
@Table(name = "quiz_questions")
public class QuizQuestion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "quiz_id", nullable = false)
    private Quiz quiz;

    @Column(name = "question_text", columnDefinition = "TEXT", nullable = false)
    private String questionText;

    @Column(name = "option_a") private String optionA;
    @Column(name = "option_b") private String optionB;
    @Column(name = "option_c") private String optionC;
    @Column(name = "option_d") private String optionD;

    @Column(name = "correct_option")
    private String correctOption;

    @Column(name = "explanation", columnDefinition = "TEXT")
    private String explanation;

    public QuizQuestion() {}
    public QuizQuestion(Long id, Quiz quiz, String questionText, String optionA, String optionB, String optionC, String optionD, String correctOption, String explanation) {
        this.id = id; this.quiz = quiz; this.questionText = questionText;
        this.optionA = optionA; this.optionB = optionB; this.optionC = optionC; this.optionD = optionD;
        this.correctOption = correctOption; this.explanation = explanation;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Quiz getQuiz() { return quiz; }
    public void setQuiz(Quiz quiz) { this.quiz = quiz; }
    public String getQuestionText() { return questionText; }
    public void setQuestionText(String q) { this.questionText = q; }
    public String getOptionA() { return optionA; }
    public void setOptionA(String o) { this.optionA = o; }
    public String getOptionB() { return optionB; }
    public void setOptionB(String o) { this.optionB = o; }
    public String getOptionC() { return optionC; }
    public void setOptionC(String o) { this.optionC = o; }
    public String getOptionD() { return optionD; }
    public void setOptionD(String o) { this.optionD = o; }
    public String getCorrectOption() { return correctOption; }
    public void setCorrectOption(String c) { this.correctOption = c; }
    public String getExplanation() { return explanation; }
    public void setExplanation(String e) { this.explanation = e; }

    public static QuizQuestionBuilder builder() { return new QuizQuestionBuilder(); }
    public static class QuizQuestionBuilder {
        private Quiz quiz; private String questionText; private String optionA; private String optionB;
        private String optionC; private String optionD; private String correctOption; private String explanation;
        public QuizQuestionBuilder quiz(Quiz q) { this.quiz = q; return this; }
        public QuizQuestionBuilder questionText(String t) { this.questionText = t; return this; }
        public QuizQuestionBuilder optionA(String o) { this.optionA = o; return this; }
        public QuizQuestionBuilder optionB(String o) { this.optionB = o; return this; }
        public QuizQuestionBuilder optionC(String o) { this.optionC = o; return this; }
        public QuizQuestionBuilder optionD(String o) { this.optionD = o; return this; }
        public QuizQuestionBuilder correctOption(String c) { this.correctOption = c; return this; }
        public QuizQuestionBuilder explanation(String e) { this.explanation = e; return this; }
        public QuizQuestion build() {
            QuizQuestion q = new QuizQuestion();
            q.quiz = quiz; q.questionText = questionText; q.optionA = optionA; q.optionB = optionB;
            q.optionC = optionC; q.optionD = optionD; q.correctOption = correctOption; q.explanation = explanation;
            return q;
        }
    }
}
