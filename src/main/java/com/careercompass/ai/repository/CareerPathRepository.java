package com.careercompass.ai.repository;

import com.careercompass.ai.model.CareerPath;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CareerPathRepository extends JpaRepository<CareerPath, Long> {
}
