package com.careercompass.ai.config;

import com.careercompass.ai.model.*;
import com.careercompass.ai.repository.*;
import java.time.LocalDateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Seeds the admin user on application startup if it does not already exist.
 * Admin email: shannu1@gmail.com
 */
@Configuration
@SuppressWarnings("null")
public class AdminSeeder {

    private static final Logger log = LoggerFactory.getLogger(AdminSeeder.class);

    private static final String ADMIN_EMAIL = "shannu1@gmail.com";
    private static final String ADMIN_NAME = "Admin";
    private static final String ADMIN_DEFAULT_PASSWORD = "admin@123"; // Change in production!

    @Bean
    public CommandLineRunner seedDatabase(UserRepository userRepository, ProblemRepository problemRepository, ContestRepository contestRepository, PasswordEncoder encoder) {
        return args -> {
            // 1. Seed Admin
            seedAdminUser(userRepository, encoder);
            
            // 2. Seed Problems
            if (problemRepository.count() == 0) {
                seedProblems(problemRepository);
            }

            // 3. Seed Contest
            if (contestRepository.count() == 0) {
                seedContest(contestRepository, problemRepository);
            }
        };
    }

    private void seedAdminUser(UserRepository userRepository, PasswordEncoder encoder) {
        var existing = userRepository.findByEmail(ADMIN_EMAIL);
        if (existing.isPresent()) {
            User user = existing.get();
            if (user.getRole() != Role.ADMIN) {
                user.setRole(Role.ADMIN);
                user.setEmailVerified(true);
                userRepository.save(user);
                log.info("EXISTING USER PROMOTED TO ADMIN: {}", ADMIN_EMAIL);
            }
            return;
        }

        User admin = User.builder()
                .name(ADMIN_NAME)
                .email(ADMIN_EMAIL)
                .password(encoder.encode(ADMIN_DEFAULT_PASSWORD))
                .role(Role.ADMIN)
                .emailVerified(true)
                .failedLoginAttempts(0)
                .mfaEnabled(false)
                .tokenVersion(0)
                .xp(0)
                .level(1)
                .build();

        userRepository.save(admin);
        log.info("ADMIN USER CREATED. Email: {}, Password: {}", ADMIN_EMAIL, ADMIN_DEFAULT_PASSWORD);
    }

    private void seedProblems(ProblemRepository problemRepository) {
        Problem p1 = Problem.builder()
                .title("Two Sum Architecture")
                .description("Given an array of integers nums and an integer target, return indices of the two numbers such that they add up to target. Optimize for O(n) complexity.")
                .difficulty(Difficulty.EASY)
                .tags("Arrays, Hash Table, Optimization")
                .baseCode("class Solution {\n    public int[] twoSum(int[] nums, int target) {\n        // Your implementation\n        return new int[]{};\n    }\n}")
                .xpReward(100)
                .build();

        Problem p2 = Problem.builder()
                .title("Neural Path Finder")
                .description("Given a grid of size m x n, calculate the number of unique paths from the top-left to the bottom-right node. You can only move down or right.")
                .difficulty(Difficulty.MEDIUM)
                .tags("Dynamic Programming, Grid, Combinatorics")
                .baseCode("class Solution {\n    public int uniquePaths(int m, int n) {\n        // Your implementation\n        return 0;\n    }\n}")
                .xpReward(250)
                .build();

        problemRepository.save(p1);
        problemRepository.save(p2);
        log.info("Problem Library seeded successfully.");
    }

    private void seedContest(ContestRepository contestRepository, ProblemRepository problemRepository) {
        var problems = problemRepository.findAll();
        Contest contest = Contest.builder()
                .name("Global Neural Selection v1.0")
                .startTime(LocalDateTime.now())
                .endTime(LocalDateTime.now().plusDays(7))
                .difficulty(Difficulty.MEDIUM)
                .problems(problems)
                .build();
        
        contestRepository.save(contest);
        log.info("Weekly Contest initialized.");
    }
}
