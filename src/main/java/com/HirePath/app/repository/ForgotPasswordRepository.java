package com.HirePath.app.repository;

import com.HirePath.app.entity.ForgotPasswordOtp;
import com.HirePath.app.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ForgotPasswordRepository extends JpaRepository<ForgotPasswordOtp, Long> {

    public ForgotPasswordOtp findByUser(User user);

}
