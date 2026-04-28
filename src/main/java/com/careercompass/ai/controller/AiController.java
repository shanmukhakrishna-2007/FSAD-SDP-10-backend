package com.careercompass.ai.controller;

import com.careercompass.ai.model.User;
import com.careercompass.ai.service.GeminiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.HashMap;

@RestController
@RequestMapping("/api/ai")
public class AiController {

    private final GeminiService geminiService;

    @Autowired
    public AiController(GeminiService geminiService) {
        this.geminiService = geminiService;
    }

    @PostMapping("/chat")
    public ResponseEntity<Map<String, String>> chat(@RequestBody Map<String, String> request) {
        String message = request.get("message");
        String history = request.get("history");
        if (message == null || message.trim().isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Message is required"));
        }
        
        String response = geminiService.chatAgent(message, history);
        
        Map<String, String> result = new HashMap<>();
        result.put("response", response);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/interview/start")
    public ResponseEntity<Map<String, String>> startInterview(@RequestBody Map<String, String> request) {
        String role = request.get("role");
        String company = request.get("company");
        String question = geminiService.getInterviewQuestion(role, company);
        return ResponseEntity.ok(Map.of("question", question));
    }

    @PostMapping("/interview/respond")
    public ResponseEntity<Map<String, String>> respondInterview(@RequestBody Map<String, String> request) {
        String question = request.get("question");
        String answer = request.get("answer");
        String role = request.get("role");
        String feedback = geminiService.getInterviewFeedback(question, answer, role);
        return ResponseEntity.ok(Map.of("feedback", feedback));
    }

    @PostMapping("/resume/analyze")
    public ResponseEntity<Map<String, String>> analyzeResume(@RequestBody Map<String, String> request) {
        String resumeText = request.get("resumeText");
        String role = request.get("role");
        String analysis = geminiService.analyzeResume(resumeText, role);
        return ResponseEntity.ok(Map.of("analysis", analysis));
    }

    @PostMapping("/company/intel")
    public ResponseEntity<Map<String, String>> getCompanyIntel(@RequestBody Map<String, String> request) {
        String company = request.get("company");
        String intel = geminiService.getCompanyIntelligence(company);
        return ResponseEntity.ok(Map.of("intel", intel));
    }
    
    @PostMapping("/roadmap")
    public ResponseEntity<Map<String, Object>> generateRoadmap(@RequestBody Map<String, String> request) {
        String goal = request.get("goal");
        if (goal == null || goal.trim().isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Goal is required"));
        }
        
        String jsonArrayString = geminiService.generateCustomRoadmap(goal);
        
        Map<String, Object> result = new HashMap<>();
        result.put("roadmapJson", jsonArrayString);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/code/analyze")
    public ResponseEntity<Map<String, String>> analyzeCode(@RequestBody Map<String, String> request) {
        String problemDesc = request.get("problemDesc");
        String code = request.get("code");
        String status = request.get("status");
        String analysis = geminiService.analyzeCodeSubmission(problemDesc, code, status);
        return ResponseEntity.ok(Map.of("analysis", analysis));
    }

    @GetMapping("/advice")
    public ResponseEntity<Map<String, String>> getAdvice(@AuthenticationPrincipal User user) {
        if (user == null) return ResponseEntity.status(401).build();
        // In a real scenario, we'd fetch solved tags from DB. For now using a placeholder.
        String advice = geminiService.getPersonalizedAdvice(user.getName(), user.getLevel(), user.getXp(), "General DSA, Java");
        return ResponseEntity.ok(Map.of("advice", advice));
    }
}
