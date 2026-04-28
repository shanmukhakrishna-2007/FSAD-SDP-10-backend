package com.careercompass.ai.controller;

import com.careercompass.ai.model.Certificate;
import com.careercompass.ai.model.Course;
import com.careercompass.ai.model.User;
import com.careercompass.ai.repository.CertificateRepository;
import com.careercompass.ai.repository.CourseRepository;
import com.careercompass.ai.repository.UserRepository;
import com.careercompass.ai.service.GeminiService;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/courses")
@SuppressWarnings("null")
public class CourseController {

    private final CourseRepository courseRepo;
    private final CertificateRepository certRepo;
    private final UserRepository userRepo;
    private final GeminiService geminiService;

    public CourseController(CourseRepository courseRepo, CertificateRepository certRepo, 
                           UserRepository userRepo, GeminiService geminiService) {
        this.courseRepo = courseRepo;
        this.certRepo = certRepo;
        this.userRepo = userRepo;
        this.geminiService = geminiService;
    }

    @GetMapping
    public ResponseEntity<List<Map<String, Object>>> getAllCourses() {
        return ResponseEntity.ok(courseRepo.findAll().stream().map(this::toMap).collect(Collectors.toList()));
    }

    @GetMapping("/trending")
    public ResponseEntity<List<Map<String, Object>>> getTrending() {
        return ResponseEntity.ok(courseRepo.findByTrendingTrue().stream().map(this::toMap).collect(Collectors.toList()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getCourse(@PathVariable Long id) {
        Course c = courseRepo.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Course not found"));
        Map<String, Object> m = toMap(c);
        m.put("pathName", c.getCareerPath() != null ? c.getCareerPath().getName() : null);
        return ResponseEntity.ok(m);
    }

    /**
     * Generate an AI-powered quiz for a specific course's topics.
     * Returns a JSON array of 5 quiz questions.
     */
    @PostMapping("/{id}/generate-quiz")
    public ResponseEntity<Map<String, Object>> generateCourseQuiz(@PathVariable Long id) {
        Course course = courseRepo.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Course not found"));

        JsonNode quizJson = geminiService.generateQuizJson(course.getTitle() + " covering: " + course.getTopics());
        
        Map<String, Object> result = new HashMap<>();
        if (quizJson != null && quizJson.isArray()) {
            List<Map<String, String>> questions = new ArrayList<>();
            for (JsonNode q : quizJson) {
                Map<String, String> question = new HashMap<>();
                question.put("questionText", q.has("questionText") ? q.get("questionText").asText() : "");
                question.put("optionA", q.has("optionA") ? q.get("optionA").asText() : "");
                question.put("optionB", q.has("optionB") ? q.get("optionB").asText() : "");
                question.put("optionC", q.has("optionC") ? q.get("optionC").asText() : "");
                question.put("optionD", q.has("optionD") ? q.get("optionD").asText() : "");
                question.put("correctOption", q.has("correctOption") ? q.get("correctOption").asText() : "A");
                question.put("explanation", q.has("explanation") ? q.get("explanation").asText() : "");
                questions.add(question);
            }
            result.put("questions", questions);
            result.put("courseTitle", course.getTitle());
        } else {
            // Fallback: generate static placeholder questions
            result.put("questions", generateFallbackQuiz(course));
            result.put("courseTitle", course.getTitle());
            result.put("fallback", true);
        }
        return ResponseEntity.ok(result);
    }

    /**
     * Complete a course after passing the quiz.
     * Requires quizScore >= 70. Generates a certificate.
     */
    @PostMapping("/{id}/complete")
    public ResponseEntity<Map<String, Object>> completeCourse(
            @PathVariable Long id,
            @RequestBody Map<String, Object> body,
            Authentication auth) {
        
        User user = userRepo.findByEmail(auth.getName())
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        Course course = courseRepo.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Course not found"));

        // Validate quiz score
        int quizScore = 0;
        if (body.get("quizScore") instanceof Number) {
            quizScore = ((Number) body.get("quizScore")).intValue();
        }
        
        if (quizScore < 70) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, 
                "Quiz score must be at least 70% to earn certification. Your score: " + quizScore + "%");
        }

        // Check if cert already exists for this user + course
        List<Certificate> existing = certRepo.findByUserOrderByIssuedAtDesc(user);
        for (Certificate c : existing) {
            if (c.getCourse() != null && c.getCourse().getId().equals(course.getId())) {
                Map<String, Object> result = new HashMap<>();
                result.put("certificateId", c.getId());
                result.put("message", "Certificate already issued for this course.");
                result.put("alreadyCompleted", true);
                return ResponseEntity.ok(result);
            }
        }

        // Generate certificate
        Certificate cert = Certificate.builder()
                .user(user)
                .course(course)
                .certificateTitle(course.getTitle())
                .studentName(user.getName())
                .verificationStatement(geminiService.generateCertificateVerification(user.getName(), course.getTitle()))
                .build();

        certRepo.save(cert);

        // Award XP
        user.setXp(user.getXp() + 50);
        user.setLevel(1 + (user.getXp() / 1000));
        userRepo.save(user);

        Map<String, Object> result = new HashMap<>();
        result.put("certificateId", cert.getId());
        result.put("message", "Course completed! Certificate issued.");
        result.put("xpGained", 50);
        return ResponseEntity.ok(result);
    }

    private List<Map<String, String>> generateFallbackQuiz(Course course) {
        String[] topics = course.getTopics() != null ? course.getTopics().split(",") : new String[]{"General"};
        List<Map<String, String>> questions = new ArrayList<>();
        for (int i = 0; i < Math.min(5, topics.length); i++) {
            Map<String, String> q = new HashMap<>();
            q.put("questionText", "What is the primary purpose of " + topics[i].trim() + "?");
            q.put("optionA", "To handle data processing");
            q.put("optionB", "To manage system architecture");
            q.put("optionC", "To optimize performance");
            q.put("optionD", "To improve developer experience");
            q.put("correctOption", "A");
            q.put("explanation", topics[i].trim() + " is a key concept in " + course.getTitle());
            questions.add(q);
        }
        return questions;
    }

    private Map<String, Object> toMap(Course c) {
        Map<String, Object> m = new HashMap<>();
        m.put("id", c.getId());
        m.put("title", c.getTitle());
        m.put("description", c.getDescription());
        m.put("difficulty", c.getDifficulty());
        m.put("durationHours", c.getDurationHours());
        m.put("topics", c.getTopics());
        m.put("trending", c.isTrending());
        m.put("careerPathId", c.getCareerPath() != null ? c.getCareerPath().getId() : null);
        return m;
    }
}
