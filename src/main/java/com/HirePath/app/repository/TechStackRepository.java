package com.HirePath.app.repository;

import com.HirePath.app.entity.TechStack;
import com.HirePath.app.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TechStackRepository extends JpaRepository<TechStack, Long> {

            TechStack findByUser(User user);

}
