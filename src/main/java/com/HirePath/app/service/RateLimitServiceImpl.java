package com.HirePath.app.service;

import com.HirePath.app.entity.User;
import com.HirePath.app.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class RateLimitServiceImpl implements RateLimitService{

    private final UserRepository userRepository;

    public RateLimitServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    @Override
    public void checkAndIncrement(User user) throws Exception {

        User freshUser = userRepository.findById(user.getId())
                .orElseThrow(() -> new Exception("User not found"));

        LocalDate today = LocalDate.now();

        // ✅ Reset count if it's a new day
        if (freshUser.getLastApiCallDate() == null ||
                !freshUser.getLastApiCallDate().equals(today)) {
            freshUser.setDailyApiCallCount(0);
            freshUser.setLastApiCallDate(today);
        }

        // ✅ Check limit
        if (freshUser.getDailyApiCallCount() >= 10) {
            throw new Exception("Daily limit of " + 10
                    + " API calls reached. Please try again tomorrow.");
        }

        // ✅ Increment and save
        freshUser.setDailyApiCallCount(freshUser.getDailyApiCallCount() + 1);
        userRepository.save(freshUser);
    }

}


