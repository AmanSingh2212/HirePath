package com.HirePath.app.controller;

import com.HirePath.app.entity.TechStack;
import com.HirePath.app.entity.User;
import com.HirePath.app.request.TechStackRequest;
import com.HirePath.app.service.TechStackService;
import com.HirePath.app.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users/techStack")
public class TechStackController {

    private final TechStackService techStackService;
    private final UserService userService;

    public TechStackController(TechStackService techStackService, UserService userService) {
        this.techStackService = techStackService;
        this.userService = userService;
    }


    @PostMapping("/add")
    private ResponseEntity<TechStack> add(@RequestBody TechStackRequest techStackRequest,
                                          @RequestHeader("Authorization") String jwt) throws Exception {

              User user = userService.findByJwtToken(jwt);

              techStackRequest.setUser(user);

              TechStack techStack1 = techStackService.addTechStack(techStackRequest, user);

              return new ResponseEntity<>(techStack1, HttpStatus.CREATED);

    }

    @PostMapping("/addItem")
    private ResponseEntity<TechStack> add(@RequestParam String tech,

                                          @RequestHeader("Authorization") String jwt) throws Exception {

        User user = userService.findByJwtToken(jwt);

        TechStack techStack1 = techStackService.addTech(user, tech);

        return new ResponseEntity<>(techStack1, HttpStatus.CREATED);

    }

    @PostMapping("/removeItem")
    private ResponseEntity<TechStack> remove(@RequestParam String tech,

                                          @RequestHeader("Authorization") String jwt) throws Exception {

        User user = userService.findByJwtToken(jwt);

        TechStack techStack1 = techStackService.removeTech(user, tech);

        return new ResponseEntity<>(techStack1, HttpStatus.CREATED);

    }

}
