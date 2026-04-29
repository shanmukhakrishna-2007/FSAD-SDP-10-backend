package com.careercompass.ai.controller;

import com.careercompass.ai.model.Contest;
import com.careercompass.ai.repository.ContestRepository;
import com.careercompass.ai.repository.SubmissionRepository;
import com.careercompass.ai.model.SubmissionStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/contests")

public class ContestController {

    private final ContestRepository contestRepository;
    private final SubmissionRepository submissionRepository;

    public ContestController(ContestRepository contestRepository, SubmissionRepository submissionRepository) {
        this.contestRepository = contestRepository;
        this.submissionRepository = submissionRepository;
    }

    @GetMapping("/active")
    public ResponseEntity<List<Contest>> getActiveContests() {
        return ResponseEntity.ok(contestRepository.findActiveContests(LocalDateTime.now()));
    }

    @GetMapping("/{id}/leaderboard")
    public ResponseEntity<List<Map<String, Object>>> getLeaderboard(@PathVariable Long id) {
        Contest contest = contestRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Contest not found"));
        
        // Compute leaderboard based on accepted submissions for problems in this contest
        // This is a simplified computation
        var problems = contest.getProblems();
        var allSubmissions = submissionRepository.findAll(); // In production, we'd filter by time and problem set

        Map<String, Long> userPoints = allSubmissions.stream()
                .filter(s -> problems.contains(s.getProblem()))
                .filter(s -> s.getStatus() == SubmissionStatus.ACCEPTED)
                .collect(Collectors.groupingBy(
                        s -> s.getUser().getName(),
                        Collectors.counting()
                ));

        List<Map<String, Object>> leaderboard = userPoints.entrySet().stream()
                .sorted((e1, e2) -> e2.getValue().compareTo(e1.getValue()))
                .map(e -> Map.of("name", (Object)e.getKey(), "score", (Object)(e.getValue() * 100)))
                .collect(Collectors.toList());

        return ResponseEntity.ok(leaderboard);
    }
}
