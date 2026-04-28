package com.careercompass.ai.repository;

import com.careercompass.ai.model.TestCase;
import com.careercompass.ai.model.Problem;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface TestCaseRepository extends JpaRepository<TestCase, Long> {
    List<TestCase> findByProblem(Problem problem);
    List<TestCase> findByProblemId(Long problemId);
    List<TestCase> findByProblemAndIsHiddenFalse(Problem problem);
}
