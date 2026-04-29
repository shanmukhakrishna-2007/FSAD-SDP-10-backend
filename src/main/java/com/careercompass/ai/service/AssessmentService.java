package com.careercompass.ai.service;

import com.careercompass.ai.dto.AnswerSubmitRequest;
import com.careercompass.ai.dto.QuestionDTO;
import com.careercompass.ai.dto.ResultResponse;
import com.careercompass.ai.model.*;
import com.careercompass.ai.repository.AssessmentRepository;
import com.careercompass.ai.repository.QuestionRepository;
import com.careercompass.ai.repository.ResultRepository;
import com.careercompass.ai.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service

public class AssessmentService {

    private final QuestionRepository questionRepository;
    private final AssessmentRepository assessmentRepository;
    private final ResultRepository resultRepository;
    private final UserRepository userRepository;
    private final GeminiService aiService;

    public AssessmentService(QuestionRepository questionRepository, AssessmentRepository assessmentRepository, ResultRepository resultRepository, UserRepository userRepository, GeminiService aiService) {
        this.questionRepository = questionRepository; this.assessmentRepository = assessmentRepository;
        this.resultRepository = resultRepository; this.userRepository = userRepository; this.aiService = aiService;
    }

    public List<QuestionDTO> getAllQuestions() {
        return questionRepository.findAll().stream()
                .map(q -> QuestionDTO.builder()
                        .id(q.getId())
                        .text(q.getText())
                        .category(q.getCategory())
                        .build())
                .collect(Collectors.toList());
    }

    @Transactional
    public ResultResponse submitAssessment(String userEmail, List<AnswerSubmitRequest> answers) {
        User user = userRepository.findByEmail(userEmail)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        
        Assessment assessment = new Assessment();
        assessment.setUser(user);
        assessment.setStatus(AssessmentStatus.COMPLETED);

        int analytical = 0, creative = 0, technical = 0, social = 0;

        for (AnswerSubmitRequest ans : answers) {
            Question q = questionRepository.findById(ans.getQuestionId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Question not found"));
            AssessmentResponse resp = new AssessmentResponse();
            resp.setAssessment(assessment);
            resp.setQuestion(q);
            resp.setScore(ans.getScore());
            assessment.getResponses().add(resp);

            switch (q.getCategory()) {
                case ANALYTICAL: analytical += ans.getScore(); break;
                case CREATIVE: creative += ans.getScore(); break;
                case TECHNICAL: technical += ans.getScore(); break;
                case SOCIAL: social += ans.getScore(); break;
            }
        }
        
        assessmentRepository.save(assessment);

        // Calculate % scales (max 5 per question * 5 questions = 25 per category)
        int analyticalPercent = (analytical * 100) / 25;
        int creativePercent = (creative * 100) / 25;
        int technicalPercent = (technical * 100) / 25;
        int socialPercent = (social * 100) / 25;
        int totalPercent = (analyticalPercent + creativePercent + technicalPercent + socialPercent) / 4;

        // Generate AI recommendation
        String aiRec = aiService.getCareerRecommendation(analytical, creative, technical, social);

        Result result = new Result();
        result.setUser(user);
        result.setAssessment(assessment);
        result.setAnalyticalScore(analyticalPercent);
        result.setCreativeScore(creativePercent);
        result.setTechnicalScore(technicalPercent);
        result.setSocialScore(socialPercent);
        result.setTotalScore(totalPercent);
        result.setAiRecommendation(aiRec);
        
        resultRepository.save(result);

        return toResultResponse(result);
    }

    public List<ResultResponse> getUserResults(String userEmail) {
        User user = userRepository.findByEmail(userEmail)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        return resultRepository.findAllByUserOrderByCompletedAtDesc(user).stream()
                .map(this::toResultResponse)
                .collect(Collectors.toList());
    }

    public ResultResponse getResultById(Long id) {
        Result result = resultRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Result not found"));
        return toResultResponse(result);
    }

    private ResultResponse toResultResponse(Result result) {
        return ResultResponse.builder()
                .id(result.getId())
                .analyticalScore(result.getAnalyticalScore())
                .creativeScore(result.getCreativeScore())
                .technicalScore(result.getTechnicalScore())
                .socialScore(result.getSocialScore())
                .totalScore(result.getTotalScore())
                .aiRecommendation(result.getAiRecommendation())
                .date(result.getCompletedAt())
                .build();
    }
}
