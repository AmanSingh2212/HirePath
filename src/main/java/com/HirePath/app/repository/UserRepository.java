package com.HirePath.app.repository;

import com.HirePath.app.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

        User findByEmail(String email);

        boolean existsByEmail(String email);

}
