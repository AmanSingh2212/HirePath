package com.HirePath.app.repository;

import com.HirePath.app.entity.DailyPlan;
import com.HirePath.app.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.Optional;

public interface DailyPlanRepository extends JpaRepository<DailyPlan, Long> {

    @Query("SELECT dp FROM DailyPlan dp JOIN FETCH dp.questionList WHERE dp.user = :user AND dp.date = :date")
    Optional<DailyPlan> findByUserAndDate(User user, LocalDate date);

}
