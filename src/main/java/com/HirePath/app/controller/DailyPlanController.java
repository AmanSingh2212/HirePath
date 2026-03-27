package com.HirePath.app.controller;

import com.HirePath.app.config.JwtProvider;
import com.HirePath.app.dto.DailyPlanDto;
import com.HirePath.app.entity.DailyPlan;
import com.HirePath.app.entity.User;
import com.HirePath.app.service.DailyPlanService;
import com.HirePath.app.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/plans")
public class DailyPlanController {

      private final DailyPlanService dailyPlanService;
      private final UserService userService;

      public DailyPlanController(DailyPlanService dailyPlanService, UserService userService) {
        this.dailyPlanService = dailyPlanService;
          this.userService = userService;
      }

    @GetMapping("/today")
    public ResponseEntity<DailyPlanDto> getTodayPlan(
            @RequestHeader("Authorization") String jwt) throws Exception {

          User user = userService.findByJwtToken(jwt);

        return ResponseEntity.ok(dailyPlanService.getTodayPlan(user));
    }

}
