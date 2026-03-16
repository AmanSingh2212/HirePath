package com.HirePath.app.service;

import com.HirePath.app.entity.User;

public interface RateLimitService {

    void checkAndIncrement(User user) throws Exception;

}
