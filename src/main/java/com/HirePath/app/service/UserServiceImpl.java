package com.HirePath.app.service;

import com.HirePath.app.config.JwtProvider;
import com.HirePath.app.entity.User;
import com.HirePath.app.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Random;

@Service
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    @Override
    public String generateForgotPasswordOtp() throws Exception {
        return String.format("%06d", new Random().nextInt(999999));

    }

    @Override
    public boolean isForgotPasswordOtpValid(String inputOtp, String storedOtp, LocalDateTime expiryTime) throws Exception {
        return inputOtp.equals(storedOtp) && LocalDateTime.now().isBefore(expiryTime);
    }

    @Override
    public User findByJwtToken(String jwt) throws Exception {

        String email = JwtProvider.getEmailFromToken(jwt);

        User user = userRepository.findByEmail(email);

        return user;

    }
}
