package com.HirePath.app.controller;

import com.HirePath.app.entity.QuestionList;
import com.HirePath.app.entity.User;
import com.HirePath.app.service.RateLimitService;
import com.HirePath.app.service.UserService;
import com.HirePath.app.service.geminiService.RagServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ai/rag")
public class RagController {

    private final RagServiceImpl ragServiceImpl;
    private final UserService userService;
    private final RateLimitService rateLimitService;

    public RagController(RagServiceImpl ragServiceImpl, UserService userService, RateLimitService rateLimitService) {
        this.ragServiceImpl = ragServiceImpl;
        this.userService = userService;
        this.rateLimitService = rateLimitService;
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
}
