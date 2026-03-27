package com.HirePath.app.config;

import com.HirePath.app.entity.DailyPlan;
import com.HirePath.app.entity.QuestionList;
import com.HirePath.app.entity.User;
import com.HirePath.app.repository.QuestionListRepository;
import com.HirePath.app.repository.UserRepository;
import com.HirePath.app.service.DailyPlanService;
import com.HirePath.app.service.geminiService.RagService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
public class DailyPlanSchedular {

    private final UserRepository userRepository;
    private final DailyPlanService dailyPlanService;
    private final RagService ragService;


    public DailyPlanSchedular(UserRepository userRepository, DailyPlanService dailyPlanService, RagService ragService,
                              QuestionListRepository questionListRepository) {
        this.userRepository = userRepository;
        this.dailyPlanService = dailyPlanService;
        this.ragService = ragService;
    }

    @Scheduled(cron = "0 15 12 * * ?")// 2 AM daily
    public void generatePlans() throws Exception {

        System.out.println("Precomputing daily plans...");

        List<User> users = userRepository.findAll();

        for (User user : users) {

            DailyPlan plan = new DailyPlan();
            plan.setUser(user);
            plan.setDate(LocalDate.now());
            plan.setQuestionList(generatePlanContent(user));

            dailyPlanService.savePlan(plan); // saves + updates cache
        }
    }

    private QuestionList generatePlanContent(User user) throws Exception {
        // integrate your RAG / AI logic here
        QuestionList questionList = ragService.generateQuestions("generate", user);
        return questionList;
    }

}
