package com.careercompass.ai.dto;

import com.careercompass.ai.model.Category;

public class QuestionDTO {
    private Long id;
    private String text;
    private Category category;

    public QuestionDTO() {}
    public QuestionDTO(Long id, String text, Category category) {
        this.id = id; this.text = text; this.category = category;
    }

    public Long getId() { return id; } public void setId(Long id) { this.id = id; }
    public String getText() { return text; } public void setText(String text) { this.text = text; }
    public Category getCategory() { return category; } public void setCategory(Category category) { this.category = category; }

    public static QuestionDTOBuilder builder() { return new QuestionDTOBuilder(); }
    public static class QuestionDTOBuilder {
        private Long id; private String text; private Category category;
        public QuestionDTOBuilder id(Long v) { this.id = v; return this; }
        public QuestionDTOBuilder text(String v) { this.text = v; return this; }
        public QuestionDTOBuilder category(Category v) { this.category = v; return this; }
        public QuestionDTO build() { return new QuestionDTO(id, text, category); }
    }
}
