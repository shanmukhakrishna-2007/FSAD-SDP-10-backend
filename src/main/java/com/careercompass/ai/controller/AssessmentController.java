package com.careercompass.ai.controller;

import com.careercompass.ai.dto.AnswerSubmitRequest;
import com.careercompass.ai.dto.QuestionDTO;
import com.careercompass.ai.dto.ResultResponse;
import com.careercompass.ai.service.AssessmentService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/assessment")
public class AssessmentController {

    private final AssessmentService assessmentService;

    public AssessmentController(AssessmentService assessmentService) {
        this.assessmentService = assessmentService;
    }

    @GetMapping("/questions")
    public ResponseEntity<List<QuestionDTO>> getQuestions() {
        return ResponseEntity.ok(assessmentService.getAllQuestions());
    }

    @PostMapping("/submit")
    public ResponseEntity<ResultResponse> submitAssessment(
            @RequestBody List<AnswerSubmitRequest> answers,
            Authentication authentication
    ) {
        String username = authentication.getName();
        return ResponseEntity.ok(assessmentService.submitAssessment(username, answers));
    }

    @GetMapping("/results")
    public ResponseEntity<List<ResultResponse>> getUserResults(Authentication authentication) {
        String username = authentication.getName();
        return ResponseEntity.ok(assessmentService.getUserResults(username));
    }

    @GetMapping("/results/{id}")
    public ResponseEntity<ResultResponse> getResultById(@PathVariable Long id) {
        return ResponseEntity.ok(assessmentService.getResultById(id));
    }
}
