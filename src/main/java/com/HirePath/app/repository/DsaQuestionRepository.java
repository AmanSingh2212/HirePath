package com.HirePath.app.repository;

import com.HirePath.app.entity.DsaQuestion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DsaQuestionRepository extends JpaRepository<DsaQuestion, Long> {

    List<DsaQuestion> findByTitleIn(List<String> titles);
    boolean existsByTitle(String title);

}
