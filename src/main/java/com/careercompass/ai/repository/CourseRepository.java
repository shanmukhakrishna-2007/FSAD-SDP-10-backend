package com.careercompass.ai.repository;

import com.careercompass.ai.model.Course;
import com.careercompass.ai.model.CareerPath;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CourseRepository extends JpaRepository<Course, Long> {
    List<Course> findByCareerPath(CareerPath careerPath);
    List<Course> findByTrendingTrue();
}
