package com.careercompass.ai.controller;

import com.careercompass.ai.model.Quiz;
import com.careercompass.ai.model.CareerPath;
import com.careercompass.ai.model.QuizQuestion;
import com.careercompass.ai.repository.CareerPathRepository;
import com.careercompass.ai.repository.QuizRepository;
import com.careercompass.ai.service.GeminiService;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/quizzes")

public class QuizController {

    private final QuizRepository quizRepo;
    private final CareerPathRepository pathRepo;
    private final GeminiService geminiService;

    public QuizController(QuizRepository quizRepo, CareerPathRepository pathRepo, GeminiService geminiService) {
        this.quizRepo = quizRepo; this.pathRepo = pathRepo; this.geminiService = geminiService;
    }

    @GetMapping
    public ResponseEntity<List<Map<String, Object>>> getAll() {
        return ResponseEntity.ok(quizRepo.findAll().stream().map(q -> {
            Map<String, Object> m = new HashMap<>();
            m.put("id", q.getId());
            m.put("title", q.getTitle());
            m.put("companyName", q.getCompanyName());
            m.put("difficulty", q.getDifficulty());
            m.put("questionCount", q.getQuestions().size());
            return m;
        }).collect(Collectors.toList()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getQuizWithQuestions(@PathVariable Long id) {
        Optional<Quiz> quizOpt = quizRepo.findById(id);
        if (quizOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Quiz quiz = quizOpt.get();
        Map<String, Object> result = new HashMap<>();
        result.put("id", quiz.getId());
        result.put("title", quiz.getTitle());
        result.put("companyName", quiz.getCompanyName());
        result.put("difficulty", quiz.getDifficulty());
        result.put("questions", quiz.getQuestions().stream().map(q -> {
            Map<String, Object> qm = new HashMap<>();
            qm.put("id", q.getId());
            qm.put("questionText", q.getQuestionText());
            qm.put("optionA", q.getOptionA());
            qm.put("optionB", q.getOptionB());
            qm.put("optionC", q.getOptionC());
            qm.put("optionD", q.getOptionD());
            qm.put("correctOption", q.getCorrectOption());
            qm.put("explanation", q.getExplanation());
            return qm;
        }).collect(Collectors.toList()));
        return ResponseEntity.ok(result);
    }
    @PostMapping("/generate")
    public ResponseEntity<Map<String, Object>> generateQuiz(@RequestBody Map<String, Long> body) {
        Long pathId = body.get("careerPathId");
        if (pathId == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Career Path ID is missing");
        }
        CareerPath path = pathRepo.findById(pathId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Career Path not found"));

        JsonNode quizJson = geminiService.generateQuizJson(path.getName());
        if (quizJson == null || !quizJson.isArray()) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to generate quiz via AI");
        }

        Quiz quiz = Quiz.builder()
                .title(path.getName() + " Mastery Quiz")
                .companyName("AI Generated")
                .careerPath(path)
                .difficulty("Intermediate")
                .questions(new ArrayList<>())
                .build();

        for (JsonNode qNode : quizJson) {
            QuizQuestion qq = QuizQuestion.builder()
                    .quiz(quiz)
                    .questionText(qNode.get("questionText").asText())
                    .optionA(qNode.get("optionA").asText())
                    .optionB(qNode.get("optionB").asText())
                    .optionC(qNode.get("optionC").asText())
                    .optionD(qNode.get("optionD").asText())
                    .correctOption(qNode.get("correctOption").asText())
                    .explanation(qNode.get("explanation").asText())
                    .build();
            quiz.getQuestions().add(qq);
        }

        quizRepo.save(quiz);

        Map<String, Object> resp = new HashMap<>();
        resp.put("id", quiz.getId());
        resp.put("title", quiz.getTitle());
        return ResponseEntity.ok(resp);
    }
}
