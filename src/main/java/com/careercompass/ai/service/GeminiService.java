package com.careercompass.ai.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;
import java.util.List;

@Service
public class GeminiService {

    private static final Logger log = LoggerFactory.getLogger(GeminiService.class);
    private static final String FALLBACK_MSG = "AI service is temporarily unavailable. Please try again later.";

    @Value("${gemini.api.key}")
    private String apiKey;

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Core method to call Gemini API. Never returns null — returns a fallback message on any failure.
     */
    public String generateContent(String prompt) {
        if (apiKey == null || apiKey.trim().isEmpty()) {
            log.error("Gemini API Key is missing! Set gemini.api.key in application.properties");
            return FALLBACK_MSG;
        }

        String url = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash:generateContent?key=" + apiKey.trim();

        Map<String, Object> requestBody = new HashMap<>();
        Map<String, Object> contents = new HashMap<>();
        Map<String, Object> parts = new HashMap<>();
        
        parts.put("text", prompt);
        contents.put("parts", List.of(parts));
        requestBody.put("contents", List.of(contents));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

        try {
            ResponseEntity<JsonNode> response = restTemplate.postForEntity(url, entity, JsonNode.class);
            JsonNode body = response.getBody();
            
            if (body != null && body.has("candidates") && body.get("candidates").isArray() && body.get("candidates").size() > 0) {
                JsonNode candidate = body.get("candidates").get(0);
                if (candidate.has("content") && candidate.get("content").has("parts")) {
                    return candidate.get("content").get("parts").get(0).get("text").asText();
                }
            }
            
            log.warn("Gemini API returned unexpected structure: {}", body);
            return FALLBACK_MSG;
        } catch (Exception e) {
            log.error("Gemini API call failed: {}", e.getMessage());
            return FALLBACK_MSG;
        }
    }

    public String getCareerRecommendation(int analytical, int creative, int technical, int social) {
        String prompt = String.format(
            "Based on the following career assessment profile (scores out of 5), provide a short, 3-4 sentence career recommendation for a student. Be highly encouraging and mention specific 2-3 job roles. " +
            "Analytical: %d, Creative: %d, Technical: %d, Social: %d.", 
            analytical, creative, technical, social);
        
        String result = generateContent(prompt);
        return result != null ? result : "Your high technical and social attributes suggest a great fit for Technical Product Management.";
    }

    public String generateCertificateVerification(String studentName, String courseTitle) {
        String prompt = String.format(
            "Generate a unique, professional, and inspiring 2-line verification statement for a certificate awarded to %s for completing the course '%s'. Focus on their dedication and future potential.",
            studentName, courseTitle);
        
        String result = generateContent(prompt);
        return result != null ? result.trim() : "This certificate verifies the successful completion of all course requirements and demonstrates mastery of the subject matter.";
    }

    public JsonNode generateQuizJson(String topic) {
        String prompt = String.format(
            "Generate a technical quiz about '%s' in JSON format. Provide exactly 5 multiple choice questions. " +
            "The JSON should be an array of objects, each having: 'questionText', 'optionA', 'optionB', 'optionC', 'optionD', 'correctOption' (one of A, B, C, or D), and 'explanation'. " +
            "Return ONLY the raw JSON array, no other text or markdown formatting.",
            topic);
        
        String result = generateContent(prompt);
        if (result == null) return null;

        try {
            // Remove markdown code blocks if present
            String cleanJson = result.replaceAll("```json", "").replaceAll("```", "").trim();
            return objectMapper.readTree(cleanJson);
        } catch (Exception e) {
            System.err.println("Error parsing Quiz JSON: " + e.getMessage());
            return null;
        }
    }

    public String generateCaptchaCode() {
        String prompt = "Generate a random 5-character uppercase alphanumeric code. Do not include ambiguous characters like O, 0, I, or 1. RETURN ONLY THE 5 CHARACTERS, NO OTHER TEXT.";
        String result = generateContent(prompt);
        if (result == null) return null;
        
        // Sanitize: Keep only uppercase letters and numbers, pick first 5
        String sanitized = result.replaceAll("[^A-Z2-9]", "");
        if (sanitized.length() >= 5) {
            return sanitized.substring(0, 5);
        }
        return sanitized.isEmpty() ? null : sanitized;
    }
    public String chatAgent(String message, String history) {
        String prompt = String.format(
            "SYSTEM INSTRUCTION: You are the 'Neural Architect', an elite AI career strategist. " +
            "Your tone is professional, high-signal, and slightly futuristic (minimalist). " +
            "You have access to the user's career trajectory. Maintain context of the following conversation history.\n\n" +
            "HISTORY:\n%s\n\n" +
            "USER MESSAGE: %s\n\n" +
            "Response (concise, under 4 sentences):",
            history != null ? history : "No previous context.",
            message);
        
        String result = generateContent(prompt);
        return result != null ? result.trim() : "Neural connection unstable. Please try again.";
    }

    public String analyzeResume(String resumeText, String targetRole) {
        String prompt = String.format(
            "Perform a FAANG-level 'Resume Deep-Mapping' for the following. Target Role: %s. " +
            "Analyze for: 1. Impact metrics, 2. ATS keywords, 3. Architectural clarity. " +
            "Provide 3-4 bullet points of high-octane feedback. Resume Text: %s",
            targetRole, resumeText);
        
        return generateContent(prompt);
    }

    public String getInterviewQuestion(String role, String company) {
        String prompt = String.format(
            "Generate a complex, high-signal interview question for a %s role at %s. " +
            "Focus on either system design, behavioral leadership, or advanced technical concepts. " +
            "Return ONLY the question.",
            role, company);
        
        return generateContent(prompt);
    }

    public String getInterviewFeedback(String question, String answer, String role) {
        String prompt = String.format(
            "As an elite recruiter for top tech companies, provide expert feedback on this interview response. " +
            "Question: %s\nUser Answer: %s\nTarget Role: %s\n\n" +
            "Provide a score (0-100) and 2 sentences of critical refinement tips.",
            question, answer, role);
        
        return generateContent(prompt);
    }

    public String getCompanyIntelligence(String company) {
        String prompt = String.format(
            "Generate an 'Elite Selection Guide' for %s. Include: " +
            "1. Key cultural pillars, 2. Core technical stack preferences, 3. Most frequent interview focal points. " +
            "Keep it structured and data-dense. Use professional, futuristic terminology.",
            company);
        
        return generateContent(prompt);
    }

    public String analyzeCodeSubmission(String problemDesc, String userCode, String status) {
        String prompt = String.format(
            "As an expert Senior Software Engineer, analyze this coding submission.\n" +
            "Problem Description: %s\n" +
            "User's Code: %s\n" +
            "Status: %s\n\n" +
            "Provide 2-3 bullet points of feedback focusing on: 1. Time/Space Complexity, 2. Clean Code improvements, 3. How to fix it (if it failed). " +
            "Keep it concise and professional.",
            problemDesc, userCode, status);
        
        return generateContent(prompt);
    }

    public String getPersonalizedAdvice(String name, int level, int xp, String solvedTags) {
        String prompt = String.format(
            "SYSTEM: You are the AI Career Compass for %s. Current Profile: Level %d, XP %d. Solved Topics: %s.\n\n" +
            "Based on their progress, provide a 2-sentence motivational advice and suggest what they should learn next. " +
            "Use a professional, encouraging tone.",
            name, level, xp, solvedTags);
        
        return generateContent(prompt);
    }

    public String generateCustomRoadmap(String goal) {
        String prompt = String.format(
            "Generate a customized learning roadmap for a user who wants to achieve: '%s'. " +
            "Return the roadmap as a valid JSON array of strings, where each string is a milestone or step. " +
            "Provide exactly 5 steps. RETURN ONLY THE JSON ARRAY.",
            goal);
        
        String result = generateContent(prompt);
        if (result == null) return "[\"Analyze prerequisites\", \"Acquire foundational knowledge\", \"Build practical projects\", \"Network with professionals\", \"Apply for roles\"]";
        
        try {
            String cleanJson = result.replaceAll("```json", "").replaceAll("```", "").trim();
            return cleanJson;
        } catch (Exception e) {
            return "[\"Analyze prerequisites\", \"Acquire foundational knowledge\", \"Build practical projects\", \"Network with professionals\", \"Apply for roles\"]";
        }
    }
}
