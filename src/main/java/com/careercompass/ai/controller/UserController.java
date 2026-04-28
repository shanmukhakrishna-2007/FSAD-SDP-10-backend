package com.careercompass.ai.controller;

import com.careercompass.ai.dto.UserProfileResponse;
import com.careercompass.ai.model.User;
import com.careercompass.ai.repository.SubmissionRepository;
import com.careercompass.ai.repository.CertificateRepository;
import com.careercompass.ai.model.SubmissionStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private final SubmissionRepository submissionRepository;
    private final CertificateRepository certificateRepository;

    public UserController(SubmissionRepository submissionRepository, CertificateRepository certificateRepository) {
        this.submissionRepository = submissionRepository;
        this.certificateRepository = certificateRepository;
    }

    @GetMapping("/profile")
    public ResponseEntity<UserProfileResponse> getProfile(@AuthenticationPrincipal User user) {
        if (user == null) return ResponseEntity.status(401).build();

        long solvedCount = submissionRepository.findByUser(user).stream()
                .filter(s -> s.getStatus() == SubmissionStatus.ACCEPTED)
                .map(s -> s.getProblem().getId())
                .distinct()
                .count();

        long certCount = certificateRepository.findByUserOrderByIssuedAtDesc(user).size();

        return ResponseEntity.ok(UserProfileResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .role(user.getRole().name())
                .xp(user.getXp())
                .level(user.getLevel())
                .problemsSolved((int) solvedCount)
                .coursesCompleted((int) certCount) // Assuming cert per course
                .certificatesEarned((int) certCount)
                .recentBadges(new ArrayList<>())
                .build());
    }
}
