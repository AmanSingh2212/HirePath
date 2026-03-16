package com.HirePath.app.repository;

import com.HirePath.app.entity.DevProblem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DevProblemRepository extends JpaRepository<DevProblem, Long> {

    boolean existsByTitle(String title);

}
