package com.careercompass.ai.repository;

import com.careercompass.ai.model.Assessment;
import com.careercompass.ai.model.User;
import com.careercompass.ai.model.AssessmentStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AssessmentRepository extends JpaRepository<Assessment, Long> {
    Optional<Assessment> findByUserAndStatus(User user, AssessmentStatus status);
}
