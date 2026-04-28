package com.careercompass.ai.repository;

import com.careercompass.ai.model.Problem;
import com.careercompass.ai.model.Difficulty;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ProblemRepository extends JpaRepository<Problem, Long> {
    List<Problem> findByDifficulty(Difficulty difficulty);
}
