package com.careercompass.ai.controller;

import com.careercompass.ai.model.*;
import com.careercompass.ai.repository.*;
import com.careercompass.ai.service.GeminiService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/problems")
@SuppressWarnings("null")
public class ProblemController {

    private final ProblemRepository problemRepository;
    private final SubmissionRepository submissionRepository;
    private final TestCaseRepository testCaseRepository;
    private final UserRepository userRepository;
    private final GeminiService geminiService;

    public ProblemController(ProblemRepository problemRepository, SubmissionRepository submissionRepository,
                             TestCaseRepository testCaseRepository, UserRepository userRepository, GeminiService geminiService) {
        this.problemRepository = problemRepository;
        this.submissionRepository = submissionRepository;
        this.testCaseRepository = testCaseRepository;
        this.userRepository = userRepository;
        this.geminiService = geminiService;
    }

    @GetMapping
    public ResponseEntity<List<Map<String, Object>>> getAllProblems(@AuthenticationPrincipal User user) {
        List<Problem> problems = problemRepository.findAll();
        
        // If user is logged in, include solved status
        Set<Long> solvedIds = new HashSet<>();
        if (user != null) {
            solvedIds = submissionRepository.findByUser(user).stream()
                .filter(s -> s.getStatus() == SubmissionStatus.ACCEPTED)
                .map(s -> s.getProblem().getId())
                .collect(Collectors.toSet());
        }

        Set<Long> finalSolvedIds = solvedIds;
        List<Map<String, Object>> result = problems.stream().map(p -> {
            Map<String, Object> m = new HashMap<>();
            m.put("id", p.getId());
            m.put("title", p.getTitle());
            m.put("description", p.getDescription());
            m.put("difficulty", p.getDifficulty() != null ? p.getDifficulty().name() : "EASY");
            m.put("tags", p.getTags());
            m.put("xpReward", p.getXpReward());
            m.put("solved", finalSolvedIds.contains(p.getId()));
            return m;
        }).collect(Collectors.toList());

        return ResponseEntity.ok(result);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Problem> getProblem(@PathVariable Long id) {
        return problemRepository.findById(id)
                .map(p -> ResponseEntity.ok(p))
                .orElse(ResponseEntity.notFound().build());
    }

    /** Get visible (non-hidden) test cases for a problem */
    @GetMapping("/{id}/testcases")
    public ResponseEntity<List<Map<String, Object>>> getTestCases(@PathVariable Long id) {
        Problem problem = problemRepository.findById(id).orElse(null);
        if (problem == null) return ResponseEntity.notFound().build();

        List<TestCase> testCases = testCaseRepository.findByProblemId(id);
        List<Map<String, Object>> result = testCases.stream()
            .filter(tc -> !tc.isHidden())
            .map(tc -> {
                Map<String, Object> m = new HashMap<>();
                m.put("id", tc.getId());
                m.put("input", tc.getInput());
                m.put("expectedOutput", tc.getExpectedOutput());
                return m;
            })
            .collect(Collectors.toList());

        return ResponseEntity.ok(result);
    }

    /** Get submission history for a problem */
    @GetMapping("/{id}/submissions")
    public ResponseEntity<List<Map<String, Object>>> getSubmissions(
            @PathVariable Long id, @AuthenticationPrincipal User user) {
        if (user == null) return ResponseEntity.status(401).build();

        List<Submission> subs = submissionRepository.findByUser(user).stream()
            .filter(s -> s.getProblem().getId().equals(id))
            .sorted(Comparator.comparing(Submission::getSubmittedAt).reversed())
            .limit(10)
            .collect(Collectors.toList());

        List<Map<String, Object>> result = subs.stream().map(s -> {
            Map<String, Object> m = new HashMap<>();
            m.put("id", s.getId());
            m.put("status", s.getStatus().name());
            m.put("language", s.getLanguage());
            m.put("xpEarned", s.getXpEarned());
            m.put("submittedAt", s.getSubmittedAt());
            return m;
        }).collect(Collectors.toList());

        return ResponseEntity.ok(result);
    }

    @PostMapping("/{id}/submit")
    public ResponseEntity<Map<String, Object>> submitSolution(
            @PathVariable Long id,
            @RequestBody Map<String, String> request,
            @AuthenticationPrincipal User user) {
        
        if (user == null) return ResponseEntity.status(401).build();

        Problem problem = problemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Problem not found"));
        String code = request.get("code");
        String language = request.get("language");

        // Get test cases for evaluation context
        List<TestCase> testCases = testCaseRepository.findByProblemId(id);
        StringBuilder testCaseInfo = new StringBuilder();
        for (TestCase tc : testCases) {
            testCaseInfo.append("Input: ").append(tc.getInput())
                       .append(" | Expected: ").append(tc.getExpectedOutput()).append("\n");
        }
        
        // Use AI to evaluate code against test cases
        String analysis = geminiService.analyzeCodeSubmission(
            problem.getDescription() + "\n\nTest Cases:\n" + testCaseInfo, 
            code, "PENDING EVALUATION");
        
        boolean isCorrect = !analysis.toLowerCase().contains("failed") 
                         && !analysis.toLowerCase().contains("incorrect")
                         && !analysis.toLowerCase().contains("wrong");

        SubmissionStatus status = isCorrect ? SubmissionStatus.ACCEPTED : SubmissionStatus.WRONG_ANSWER;
        int xpGained = isCorrect ? problem.getXpReward() : 0;

        // Build per-test-case results
        List<Map<String, Object>> testResults = new ArrayList<>();
        for (int i = 0; i < testCases.size(); i++) {
            TestCase tc = testCases.get(i);
            Map<String, Object> tr = new HashMap<>();
            tr.put("input", tc.isHidden() ? "Hidden" : tc.getInput());
            tr.put("expectedOutput", tc.isHidden() ? "Hidden" : tc.getExpectedOutput());
            tr.put("passed", isCorrect); // simplified - all pass or all fail based on AI evaluation
            tr.put("hidden", tc.isHidden());
            testResults.add(tr);
        }

        Submission submission = Submission.builder()
                .user(user)
                .problem(problem)
                .code(code)
                .language(language)
                .status(status)
                .xpEarned(xpGained)
                .build();

        submissionRepository.save(submission);

        if (isCorrect) {
            user.setXp(user.getXp() + xpGained);
            user.setLevel(1 + (user.getXp() / 1000));
            userRepository.save(user);
        }

        Map<String, Object> result = new HashMap<>();
        result.put("status", status.name());
        result.put("xpGained", xpGained);
        result.put("analysis", analysis);
        result.put("testResults", testResults);
        result.put("totalTests", testCases.size());
        result.put("passedTests", isCorrect ? testCases.size() : 0);
        return ResponseEntity.ok(result);
    }
}
