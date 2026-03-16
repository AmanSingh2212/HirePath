package com.HirePath.app.repository;

import com.HirePath.app.entity.SystemDesignQuestion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SystemDesignQuestionRepository extends JpaRepository<SystemDesignQuestion, Long> {

    boolean existsByTitle(String title);

}
