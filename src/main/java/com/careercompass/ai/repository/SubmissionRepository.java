package com.careercompass.ai.repository;

import com.careercompass.ai.model.Submission;
import com.careercompass.ai.model.User;
import com.careercompass.ai.model.Problem;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface SubmissionRepository extends JpaRepository<Submission, Long> {
    List<Submission> findByUser(User user);
    List<Submission> findByProblem(Problem problem);
    List<Submission> findByUserAndProblem(User user, Problem problem);
}
