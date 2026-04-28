package com.careercompass.ai.controller;

import com.careercompass.ai.dto.AdminDTOs.*;
import com.careercompass.ai.model.Contest;
import com.careercompass.ai.model.Course;
import com.careercompass.ai.model.Problem;
import com.careercompass.ai.service.AdminService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Admin-only controller — all endpoints are protected by SecurityConfig
 * rule: .requestMatchers("/api/admin/**").hasRole("ADMIN")
 */
@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    // ── Dashboard Stats ──

    @GetMapping("/stats")
    public ResponseEntity<Object> getStats() {
        return ResponseEntity.ok(adminService.getAdminStats());
    }

    @GetMapping("/results")
    public ResponseEntity<Object> getAllResults() {
        return ResponseEntity.ok(adminService.getAllResults());
    }

    // ═══════════════════════════════════════════
    //  COURSE MANAGEMENT
    // ═══════════════════════════════════════════

    @GetMapping("/courses")
    public ResponseEntity<List<Map<String, Object>>> getAllCourses() {
        return ResponseEntity.ok(adminService.getAllCoursesAdmin());
    }

    @PostMapping("/courses")
    public ResponseEntity<Course> createCourse(@RequestBody CreateCourseRequest request) {
        return ResponseEntity.ok(adminService.createCourse(request));
    }

    @PutMapping("/courses/{id}")
    public ResponseEntity<Course> updateCourse(@PathVariable Long id, @RequestBody CreateCourseRequest request) {
        return ResponseEntity.ok(adminService.updateCourse(id, request));
    }

    @DeleteMapping("/courses/{id}")
    public ResponseEntity<Map<String, String>> deleteCourse(@PathVariable Long id) {
        adminService.deleteCourse(id);
        return ResponseEntity.ok(Map.of("message", "Course deleted successfully"));
    }

    // ═══════════════════════════════════════════
    //  PROBLEM MANAGEMENT
    // ═══════════════════════════════════════════

    @GetMapping("/problems")
    public ResponseEntity<List<Map<String, Object>>> getAllProblems() {
        return ResponseEntity.ok(adminService.getAllProblemsAdmin());
    }

    @PostMapping("/problems")
    public ResponseEntity<Problem> createProblem(@RequestBody CreateProblemRequest request) {
        return ResponseEntity.ok(adminService.createProblem(request));
    }

    @PutMapping("/problems/{id}")
    public ResponseEntity<Problem> updateProblem(@PathVariable Long id, @RequestBody CreateProblemRequest request) {
        return ResponseEntity.ok(adminService.updateProblem(id, request));
    }

    @DeleteMapping("/problems/{id}")
    public ResponseEntity<Map<String, String>> deleteProblem(@PathVariable Long id) {
        adminService.deleteProblem(id);
        return ResponseEntity.ok(Map.of("message", "Problem deleted successfully"));
    }

    // ═══════════════════════════════════════════
    //  CONTEST MANAGEMENT
    // ═══════════════════════════════════════════

    @GetMapping("/contests")
    public ResponseEntity<List<Map<String, Object>>> getAllContests() {
        return ResponseEntity.ok(adminService.getAllContestsAdmin());
    }

    @PostMapping("/contests")
    public ResponseEntity<Contest> createContest(@RequestBody CreateContestRequest request) {
        return ResponseEntity.ok(adminService.createContest(request));
    }

    @PutMapping("/contests/{id}")
    public ResponseEntity<Contest> updateContest(@PathVariable Long id, @RequestBody CreateContestRequest request) {
        return ResponseEntity.ok(adminService.updateContest(id, request));
    }

    @DeleteMapping("/contests/{id}")
    public ResponseEntity<Map<String, String>> deleteContest(@PathVariable Long id) {
        adminService.deleteContest(id);
        return ResponseEntity.ok(Map.of("message", "Contest deleted successfully"));
    }
}
