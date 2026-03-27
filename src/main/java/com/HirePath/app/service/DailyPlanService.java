package com.HirePath.app.service;

import com.HirePath.app.dto.DailyPlanDto;
import com.HirePath.app.entity.DailyPlan;
import com.HirePath.app.entity.User;

public interface DailyPlanService {

    public DailyPlanDto getTodayPlan(User user);

    public DailyPlanDto savePlan(DailyPlan plan);

}
