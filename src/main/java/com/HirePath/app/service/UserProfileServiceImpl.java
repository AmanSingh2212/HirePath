package com.HirePath.app.service;

import com.HirePath.app.entity.User;
import com.HirePath.app.entity.UserProfile;
import com.HirePath.app.repository.UserProfileRepository;
import com.HirePath.app.repository.UserRepository;
import com.HirePath.app.request.UserProfileRequest;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserProfileServiceImpl implements UserProfileService {

    @Autowired
    private UserProfileRepository userProfileRepository;

    @Autowired
    private UserRepository userRepository;


    @Transactional
    public UserProfile createProfile(Long userId, UserProfileRequest request) {

        // ✅ Single DB call — fetch and validate in one shot
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        // ✅ Check if profile already exists — avoid duplicate creation
        if (user.getUserProfile() != null) {
            throw new RuntimeException("Profile already exists for this user. Use update instead.");
        }

        UserProfile profile = new UserProfile();
        profile.setExperienceYears(request.getExperienceYears());
        profile.setAvailableHoursPerDay(request.getAvailableHoursPerDay());
        profile.setInterviewDate(request.getInterviewDate());
        profile.setTargetCompanies(request.getTargetCompanies());
        profile.setTechStack(request.getTechStack());

        // ✅ Save profile FIRST to get its ID
        UserProfile savedProfile = userProfileRepository.save(profile);

        // ✅ Link to user and save — only ONE save at the end
        user.setUserProfile(savedProfile);
        userRepository.save(user);

        return savedProfile; // ✅ No redundant second save


    }
}