package com.HirePath.app.service.kafka;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class PlanProducerServiceImpl implements PlanProducerService{

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    private static final String TOPIC = "plan-generation";

    @Override
    public void sendPlanRequest(Long jobId) {
        kafkaTemplate.send(TOPIC, jobId.toString());
    }

}
