package com.HirePath.app.repository;

import com.HirePath.app.entity.PlanJob;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlanJobRepository extends JpaRepository<PlanJob, Long> {



}
