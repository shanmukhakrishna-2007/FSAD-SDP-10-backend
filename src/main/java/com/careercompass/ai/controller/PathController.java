package com.careercompass.ai.controller;

import com.careercompass.ai.model.CareerPath;
import com.careercompass.ai.model.Course;
import com.careercompass.ai.model.Quiz;
import com.careercompass.ai.repository.CareerPathRepository;
import com.careercompass.ai.repository.CourseRepository;
import com.careercompass.ai.repository.QuizRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/paths")

public class PathController {

    private final CareerPathRepository pathRepo;
    private final CourseRepository courseRepo;
    private final QuizRepository quizRepo;

    public PathController(CareerPathRepository pathRepo, CourseRepository courseRepo, QuizRepository quizRepo) {
        this.pathRepo = pathRepo; this.courseRepo = courseRepo; this.quizRepo = quizRepo;
    }

    @GetMapping
    public ResponseEntity<List<CareerPath>> getAllPaths() {
        return ResponseEntity.ok(pathRepo.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getPathDetails(@PathVariable Long id) {
        CareerPath path = pathRepo.findById(id).orElseThrow();
        List<Course> courses = courseRepo.findByCareerPath(path);
        List<Quiz> quizzes = quizRepo.findByCareerPath(path);

        Map<String, Object> response = new HashMap<>();
        response.put("path", path);
        response.put("courses", courses.stream().map(c -> {
            Map<String, Object> m = new HashMap<>();
            m.put("id", c.getId());
            m.put("title", c.getTitle());
            m.put("description", c.getDescription());
            m.put("difficulty", c.getDifficulty());
            m.put("durationHours", c.getDurationHours());
            m.put("topics", c.getTopics());
            m.put("trending", c.isTrending());
            return m;
        }).collect(Collectors.toList()));
        response.put("quizzes", quizzes.stream().map(q -> {
            Map<String, Object> m = new HashMap<>();
            m.put("id", q.getId());
            m.put("title", q.getTitle());
            m.put("companyName", q.getCompanyName());
            m.put("difficulty", q.getDifficulty());
            m.put("questionCount", q.getQuestions().size());
            return m;
        }).collect(Collectors.toList()));

        return ResponseEntity.ok(response);
    }
}
