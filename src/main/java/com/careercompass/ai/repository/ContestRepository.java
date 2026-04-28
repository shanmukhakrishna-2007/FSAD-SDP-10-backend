package com.careercompass.ai.repository;

import com.careercompass.ai.model.Contest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.time.LocalDateTime;
import java.util.List;

public interface ContestRepository extends JpaRepository<Contest, Long> {
    @Query("SELECT c FROM Contest c WHERE c.endTime > :now")
    List<Contest> findActiveContests(LocalDateTime now);
}
