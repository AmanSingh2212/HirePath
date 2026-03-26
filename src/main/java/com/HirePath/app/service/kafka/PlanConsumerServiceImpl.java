package com.HirePath.app.service.kafka;

import com.HirePath.app.entity.PlanJob;
import com.HirePath.app.entity.QuestionList;
import com.HirePath.app.entity.User;
import com.HirePath.app.repository.PlanJobRepository;
import com.HirePath.app.repository.UserRepository;
import com.HirePath.app.service.geminiService.RagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class PlanConsumerServiceImpl implements PlanConsumerService{


    private final PlanJobRepository jobRepository;

    private final UserRepository userRepository;

    private final RagService ragService;

    public PlanConsumerServiceImpl(PlanJobRepository jobRepository, UserRepository userRepository, RagService ragService) {
        this.jobRepository = jobRepository;
        this.userRepository = userRepository;
        this.ragService = ragService;
    }

    @Override
    @KafkaListener(topics = "plan-generation", groupId = "plan-group")
    public void consume(String jobIdStr) {

        Long jobId = Long.parseLong(jobIdStr);
        PlanJob job = jobRepository.findById(jobId).orElseThrow();

        try {
            job.setStatus("PROCESSING");
            jobRepository.save(job);

            User user = userRepository.findById(job.getUser().getId()).orElseThrow();

            QuestionList result = ragService.generateQuestions(job.getQuery(), user);

            job.setResult(result);
            job.setStatus("COMPLETED");

        } catch (Exception e) {
            job.setStatus("FAILED");
            job.setErrorMessage(e.getMessage());
        }

        jobRepository.save(job);
    }

}
