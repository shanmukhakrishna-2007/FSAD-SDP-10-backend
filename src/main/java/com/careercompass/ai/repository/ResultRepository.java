package com.careercompass.ai.repository;

import com.careercompass.ai.model.Result;
import com.careercompass.ai.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ResultRepository extends JpaRepository<Result, Long> {
    List<Result> findAllByUserOrderByCompletedAtDesc(User user);
    Optional<Result> findTopByUserOrderByCompletedAtDesc(User user);

    @Query("SELECT AVG(r.analyticalScore) FROM Result r")
    Double getAverageAnalyticalScore();

    @Query("SELECT AVG(r.creativeScore) FROM Result r")
    Double getAverageCreativeScore();

    @Query("SELECT AVG(r.technicalScore) FROM Result r")
    Double getAverageTechnicalScore();

    @Query("SELECT AVG(r.socialScore) FROM Result r")
    Double getAverageSocialScore();
}
