package com.HirePath.app.service;

import com.HirePath.app.dto.DailyPlanDto;
import com.HirePath.app.dto.DevProblemDTO;
import com.HirePath.app.dto.DsaQuestionDTO;
import com.HirePath.app.dto.SystemDesignProblemDTO;
import com.HirePath.app.entity.DailyPlan;
import com.HirePath.app.entity.QuestionList;
import com.HirePath.app.entity.User;
import com.HirePath.app.repository.DailyPlanRepository;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class DailyPlanServiceImpl implements DailyPlanService{

    private final DailyPlanRepository dailyPlanRepository;

    public DailyPlanServiceImpl(DailyPlanRepository dailyPlanRepository) {
        this.dailyPlanRepository = dailyPlanRepository;
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "dailyPlans", key = "#userId + '_' + T(java.time.LocalDate).now()")
    @Override
    public DailyPlanDto getTodayPlan(User user) {

        DailyPlan plan = dailyPlanRepository.findByUserAndDate(user, LocalDate.now())
                .orElseThrow(() -> new RuntimeException("Not found"));

        QuestionList ql = plan.getQuestionList();

        List<DsaQuestionDTO> dsa = ql.getSelected_questions()
                .stream().map(q -> new DsaQuestionDTO(q.getId(), q.getTitle()))
                .toList();

        List<SystemDesignProblemDTO> sd = ql.getSystem_design_questions()
                .stream().map(q -> new SystemDesignProblemDTO(q.getId(), q.getTitle()))
                .toList();

        List<DevProblemDTO> dev = ql.getDevelopment_problems()
                .stream().map(q -> new DevProblemDTO(q.getId(), q.getTitle()))
                .toList();

        return new DailyPlanDto(user.getId(), dsa, dev, sd);
    }

    @CachePut(value = "dailyPlans",
            key = "#userId + '_' + T(java.time.LocalDate).now()")
    public DailyPlanDto savePlan(DailyPlan plan) {

        DailyPlan saved = dailyPlanRepository.save(plan);

        QuestionList ql = plan.getQuestionList();

        List<DsaQuestionDTO> dsa = ql.getSelected_questions()
                .stream()
                .map(q -> new DsaQuestionDTO(q.getId(), q.getTitle()))
                .toList();

        List<SystemDesignProblemDTO> sd = ql.getSystem_design_questions()
                .stream()
                .map(q -> new SystemDesignProblemDTO(q.getId(), q.getTitle()))
                .toList();

        List<DevProblemDTO> dev = ql.getDevelopment_problems()
                .stream()
                .map(q -> new DevProblemDTO(q.getId(), q.getTitle()))
                .toList();

        return new DailyPlanDto(plan.getUser().getId(), dsa, dev, sd);
    }

}
