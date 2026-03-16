package com.HirePath.app.controller;

import com.HirePath.app.entity.User;
import com.HirePath.app.entity.UserProfile;
import com.HirePath.app.request.UserProfileRequest;
import com.HirePath.app.service.UserProfileService;
import com.HirePath.app.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

       private final UserService userService;
       private final UserProfileService userProfileService;

    public UserController(UserService userService, UserProfileService userProfileService) {
        this.userService = userService;
        this.userProfileService = userProfileService;
    }


    @PostMapping("/addDetails")
    private ResponseEntity<User> addData(@RequestBody UserProfileRequest userProfileRequest,
                                         @RequestHeader("Authorization") String jwt) throws Exception {

          User user = userService.findByJwtToken(jwt);

          UserProfile userProfile = userProfileService.createProfile(user.getId(), userProfileRequest);

          user.setUserProfile(userProfile);

          return new ResponseEntity<>(user, HttpStatus.OK);

    }

}
