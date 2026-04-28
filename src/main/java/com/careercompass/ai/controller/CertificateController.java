package com.careercompass.ai.controller;

import com.careercompass.ai.model.Certificate;
import com.careercompass.ai.model.Course;
import com.careercompass.ai.model.User;
import com.careercompass.ai.repository.CertificateRepository;
import com.careercompass.ai.repository.CourseRepository;
import com.careercompass.ai.repository.UserRepository;
import com.careercompass.ai.service.GeminiService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/certificates")
@SuppressWarnings("null")
public class CertificateController {

    private final CertificateRepository certRepo;
    private final CourseRepository courseRepo;
    private final UserRepository userRepo;
    private final GeminiService geminiService;

    public CertificateController(CertificateRepository certRepo, CourseRepository courseRepo, UserRepository userRepo, GeminiService geminiService) {
        this.certRepo = certRepo; this.courseRepo = courseRepo; this.userRepo = userRepo; this.geminiService = geminiService;
    }

    @GetMapping
    public ResponseEntity<List<Map<String, Object>>> getMyCerts(Authentication auth) {
        User user = userRepo.findByEmail(auth.getName())
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        return ResponseEntity.ok(certRepo.findByUserOrderByIssuedAtDesc(user).stream().map(this::toMap).collect(Collectors.toList()));
    }

    @PostMapping("/generate")
    public ResponseEntity<Map<String, Object>> generateCert(
            @RequestBody Map<String, Long> body,
            Authentication auth
    ) {
        User user = userRepo.findByEmail(auth.getName())
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        Long courseId = body.get("courseId");
        if (courseId == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Course ID is missing");
        }
        Course course = courseRepo.findById(courseId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Course not found"));

        Certificate cert = Certificate.builder()
                .user(user)
                .course(course)
                .certificateTitle(course.getTitle())
                .studentName(user.getName())
                .verificationStatement(geminiService.generateCertificateVerification(user.getName(), course.getTitle()))
                .build();

        certRepo.save(cert);
        return ResponseEntity.ok(toMap(cert));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getCert(@PathVariable Long id) {
        Certificate cert = certRepo.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Certificate not found"));
        return ResponseEntity.ok(toMap(cert));
    }

    private Map<String, Object> toMap(Certificate c) {
        Map<String, Object> m = new HashMap<>();
        m.put("id", c.getId());
        m.put("certificateId", c.getCertificateId());
        m.put("certificateTitle", c.getCertificateTitle());
        m.put("studentName", c.getStudentName());
        m.put("issuedAt", c.getIssuedAt());
        m.put("courseId", c.getCourse() != null ? c.getCourse().getId() : null);
        m.put("verificationStatement", c.getVerificationStatement());
        return m;
    }
}
