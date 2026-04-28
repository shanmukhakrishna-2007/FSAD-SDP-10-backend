package com.careercompass.ai.service;

import com.careercompass.ai.dto.AdminDTOs.*;
import com.careercompass.ai.model.*;
import com.careercompass.ai.repository.*;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.*;

@Service
@SuppressWarnings("null")
public class AdminService {

    private final UserRepository userRepository;
    private final ResultRepository resultRepository;
    private final CourseRepository courseRepository;
    private final CareerPathRepository careerPathRepository;
    private final ProblemRepository problemRepository;
    private final TestCaseRepository testCaseRepository;
    private final ContestRepository contestRepository;

    public AdminService(UserRepository userRepository, ResultRepository resultRepository,
                        CourseRepository courseRepository, CareerPathRepository careerPathRepository,
                        ProblemRepository problemRepository, TestCaseRepository testCaseRepository,
                        ContestRepository contestRepository) {
        this.userRepository = userRepository;
        this.resultRepository = resultRepository;
        this.courseRepository = courseRepository;
        this.careerPathRepository = careerPathRepository;
        this.problemRepository = problemRepository;
        this.testCaseRepository = testCaseRepository;
        this.contestRepository = contestRepository;
    }

    // ── Stats (existing) ──

    public Map<String, Object> getAdminStats() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalUsers", userRepository.count());
        stats.put("totalCourses", courseRepository.count());
        stats.put("totalProblems", problemRepository.count());
        stats.put("totalContests", contestRepository.count());
        
        Map<String, Double> averages = new HashMap<>();
        averages.put("Analytical", resultRepository.getAverageAnalyticalScore());
        averages.put("Creative", resultRepository.getAverageCreativeScore());
        averages.put("Technical", resultRepository.getAverageTechnicalScore());
        averages.put("Social", resultRepository.getAverageSocialScore());
        
        stats.put("averageScores", averages);
        return stats;
    }

    public Object getAllResults() {
        return resultRepository.findAll();
    }

    // ═══════════════════════════════════════════
    //  COURSE CRUD
    // ═══════════════════════════════════════════

    public List<Map<String, Object>> getAllCoursesAdmin() {
        return courseRepository.findAll().stream().map(this::courseToMap).toList();
    }

    public Course createCourse(CreateCourseRequest req) {
        Course c = new Course();
        c.setTitle(req.getTitle());
        c.setDescription(req.getDescription());
        c.setDifficulty(req.getDifficulty());
        c.setDurationHours(req.getDurationHours());
        c.setTopics(req.getTopics());
        c.setTrending(req.isTrending());

        if (req.getCareerPathId() != null) {
            CareerPath path = careerPathRepository.findById(req.getCareerPathId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Career path not found"));
            c.setCareerPath(path);
        }

        return courseRepository.save(c);
    }

    public Course updateCourse(Long id, CreateCourseRequest req) {
        Course c = courseRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Course not found"));

        c.setTitle(req.getTitle());
        c.setDescription(req.getDescription());
        c.setDifficulty(req.getDifficulty());
        c.setDurationHours(req.getDurationHours());
        c.setTopics(req.getTopics());
        c.setTrending(req.isTrending());

        if (req.getCareerPathId() != null) {
            CareerPath path = careerPathRepository.findById(req.getCareerPathId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Career path not found"));
            c.setCareerPath(path);
        } else {
            c.setCareerPath(null);
        }

        return courseRepository.save(c);
    }

    public void deleteCourse(Long id) {
        if (!courseRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Course not found");
        }
        courseRepository.deleteById(id);
    }

    // ═══════════════════════════════════════════
    //  PROBLEM CRUD
    // ═══════════════════════════════════════════

    public List<Map<String, Object>> getAllProblemsAdmin() {
        return problemRepository.findAll().stream().map(this::problemToMap).toList();
    }

    public Problem createProblem(CreateProblemRequest req) {
        Problem p = new Problem();
        p.setTitle(req.getTitle());
        p.setDescription(req.getDescription());
        p.setDifficulty(Difficulty.valueOf(req.getDifficulty().toUpperCase()));
        p.setTags(req.getTags());
        p.setBaseCode(req.getBaseCode());
        p.setXpReward(req.getXpReward() != null ? req.getXpReward() : 100);

        Problem saved = problemRepository.save(p);

        // Save test cases if provided
        if (req.getTestCases() != null) {
            for (TestCaseDTO tc : req.getTestCases()) {
                TestCase testCase = new TestCase();
                testCase.setProblem(saved);
                testCase.setInput(tc.getInput());
                testCase.setExpectedOutput(tc.getExpectedOutput());
                testCase.setHidden(tc.isHidden());
                testCaseRepository.save(testCase);
            }
        }

        return saved;
    }

    public Problem updateProblem(Long id, CreateProblemRequest req) {
        Problem p = problemRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Problem not found"));

        p.setTitle(req.getTitle());
        p.setDescription(req.getDescription());
        p.setDifficulty(Difficulty.valueOf(req.getDifficulty().toUpperCase()));
        p.setTags(req.getTags());
        p.setBaseCode(req.getBaseCode());
        p.setXpReward(req.getXpReward() != null ? req.getXpReward() : p.getXpReward());

        Problem saved = problemRepository.save(p);

        // Replace test cases if provided
        if (req.getTestCases() != null) {
            List<TestCase> existing = testCaseRepository.findByProblemId(id);
            testCaseRepository.deleteAll(existing);

            for (TestCaseDTO tc : req.getTestCases()) {
                TestCase testCase = new TestCase();
                testCase.setProblem(saved);
                testCase.setInput(tc.getInput());
                testCase.setExpectedOutput(tc.getExpectedOutput());
                testCase.setHidden(tc.isHidden());
                testCaseRepository.save(testCase);
            }
        }

        return saved;
    }

    public void deleteProblem(Long id) {
        if (!problemRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Problem not found");
        }
        // Delete associated test cases first
        List<TestCase> testCases = testCaseRepository.findByProblemId(id);
        testCaseRepository.deleteAll(testCases);
        problemRepository.deleteById(id);
    }

    // ═══════════════════════════════════════════
    //  CONTEST CRUD
    // ═══════════════════════════════════════════

    public List<Map<String, Object>> getAllContestsAdmin() {
        return contestRepository.findAll().stream().map(this::contestToMap).toList();
    }

    public Contest createContest(CreateContestRequest req) {
        Contest c = new Contest();
        c.setName(req.getName());
        c.setDifficulty(Difficulty.valueOf(req.getDifficulty().toUpperCase()));

        if (req.isWeekly()) {
            // Weekly contest: starts now, ends in 7 days
            c.setStartTime(LocalDateTime.now());
            c.setEndTime(LocalDateTime.now().plusDays(7));
        } else {
            c.setStartTime(req.getStartTime());
            c.setEndTime(req.getEndTime());
        }

        // Attach problems
        if (req.getProblemIds() != null && !req.getProblemIds().isEmpty()) {
            List<Problem> problems = problemRepository.findAllById(req.getProblemIds());
            c.setProblems(problems);
        }

        return contestRepository.save(c);
    }

    public Contest updateContest(Long id, CreateContestRequest req) {
        Contest c = contestRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Contest not found"));

        c.setName(req.getName());
        c.setDifficulty(Difficulty.valueOf(req.getDifficulty().toUpperCase()));
        c.setStartTime(req.getStartTime());
        c.setEndTime(req.getEndTime());

        if (req.getProblemIds() != null) {
            List<Problem> problems = problemRepository.findAllById(req.getProblemIds());
            c.setProblems(problems);
        }

        return contestRepository.save(c);
    }

    public void deleteContest(Long id) {
        if (!contestRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Contest not found");
        }
        contestRepository.deleteById(id);
    }

    // ── Private Mappers ──

    private Map<String, Object> courseToMap(Course c) {
        Map<String, Object> m = new HashMap<>();
        m.put("id", c.getId());
        m.put("title", c.getTitle());
        m.put("description", c.getDescription());
        m.put("difficulty", c.getDifficulty());
        m.put("durationHours", c.getDurationHours());
        m.put("topics", c.getTopics());
        m.put("trending", c.isTrending());
        m.put("careerPathId", c.getCareerPath() != null ? c.getCareerPath().getId() : null);
        m.put("careerPathName", c.getCareerPath() != null ? c.getCareerPath().getName() : null);
        return m;
    }

    private Map<String, Object> problemToMap(Problem p) {
        Map<String, Object> m = new HashMap<>();
        m.put("id", p.getId());
        m.put("title", p.getTitle());
        m.put("description", p.getDescription());
        m.put("difficulty", p.getDifficulty() != null ? p.getDifficulty().name() : "EASY");
        m.put("tags", p.getTags());
        m.put("xpReward", p.getXpReward());
        m.put("baseCode", p.getBaseCode());
        // Include test case count
        m.put("testCaseCount", testCaseRepository.findByProblemId(p.getId()).size());
        return m;
    }

    private Map<String, Object> contestToMap(Contest c) {
        Map<String, Object> m = new HashMap<>();
        m.put("id", c.getId());
        m.put("name", c.getName());
        m.put("startTime", c.getStartTime());
        m.put("endTime", c.getEndTime());
        m.put("difficulty", c.getDifficulty() != null ? c.getDifficulty().name() : "MEDIUM");
        m.put("problemCount", c.getProblems() != null ? c.getProblems().size() : 0);
        m.put("problemIds", c.getProblems() != null ? c.getProblems().stream().map(Problem::getId).toList() : List.of());
        boolean isActive = c.getEndTime() != null && c.getEndTime().isAfter(LocalDateTime.now());
        m.put("active", isActive);
        return m;
    }
}
