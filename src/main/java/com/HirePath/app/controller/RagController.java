package com.HirePath.app.controller;

import com.HirePath.app.entity.PlanJob;
import com.HirePath.app.entity.QuestionList;
import com.HirePath.app.entity.User;
import com.HirePath.app.repository.PlanJobRepository;
import com.HirePath.app.service.RateLimitService;
import com.HirePath.app.service.UserService;
import com.HirePath.app.service.geminiService.RagServiceImpl;
import com.HirePath.app.service.kafka.PlanProducerService;
import com.HirePath.app.service.kafka.PlanProducerServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/ai/rag")
public class RagController {

    private final RagServiceImpl ragServiceImpl;
    private final UserService userService;
    private final RateLimitService rateLimitService;
    private final PlanJobRepository planJobRepository;
    private final PlanProducerService planProducerService;

    public RagController(RagServiceImpl ragServiceImpl, UserService userService, RateLimitService rateLimitService, PlanJobRepository planJobRepository, PlanProducerServiceImpl planProducerServiceImpl, PlanProducerService planProducerService) {
        this.ragServiceImpl = ragServiceImpl;
        this.userService = userService;
        this.rateLimitService = rateLimitService;
        this.planJobRepository = planJobRepository;

        this.planProducerService = planProducerService;
    }

    @GetMapping("/ask")
    public ResponseEntity<?> ask(@RequestParam String query,
                                            @RequestHeader("Authorization") String jwt) throws Exception {

        try {
            User user = userService.findByJwtToken(jwt);

            // ✅ Check rate limit BEFORE calling RAG
            rateLimitService.checkAndIncrement(user);

            QuestionList questionList = ragServiceImpl.generateQuestions(query, user);

            return new ResponseEntity<>(questionList, HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.TOO_MANY_REQUESTS); // 429
        }

    }

    @PostMapping("/generate")
    public ResponseEntity<?> generatePlan(
            @RequestParam String query,
            @RequestHeader("Authorization") String jwt) throws Exception {

        User user = userService.findByJwtToken(jwt);
        rateLimitService.checkAndIncrement(user);

        PlanJob job = new PlanJob();
        job.setStatus("PENDING");
        job.setQuery(query);
        job.setUser(user);
        planJobRepository.save(job);

        planProducerService.sendPlanRequest(job.getId());

        return ResponseEntity.ok(Map.of(
                "jobId", job.getId(),
                "status", "PENDING"
        ));
    }

    @GetMapping("/plans/{jobId}")
    public ResponseEntity<?> getPlan(@PathVariable Long jobId) {

        PlanJob job = planJobRepository.findById(jobId)
                .orElseThrow(() -> new RuntimeException("Job not found"));

        if ("COMPLETED".equals(job.getStatus())) {
            return ResponseEntity.ok(job.getResult());
        }

        return ResponseEntity.ok(Map.of(
                "status", job.getStatus(),
                "message", "Plan is still processing"
        ));
    }

}
