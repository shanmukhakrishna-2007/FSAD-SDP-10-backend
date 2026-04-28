package com.careercompass.ai.repository;

import com.careercompass.ai.model.Question;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionRepository extends JpaRepository<Question, Long> {
}
