package com.HirePath.app.service;

import com.HirePath.app.entity.UserProfile;
import com.HirePath.app.request.UserProfileRequest;

public interface UserProfileService {

    UserProfile createProfile(Long userId, UserProfileRequest request);

}
