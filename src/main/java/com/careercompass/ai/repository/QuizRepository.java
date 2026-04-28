package com.careercompass.ai.repository;

import com.careercompass.ai.model.Quiz;
import com.careercompass.ai.model.CareerPath;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface QuizRepository extends JpaRepository<Quiz, Long> {
    List<Quiz> findByCareerPath(CareerPath careerPath);
    List<Quiz> findByCompanyName(String companyName);
}
