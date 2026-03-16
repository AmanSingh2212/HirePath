package com.HirePath.app.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserProfileRequest {

    private int experienceYears;

    private int availableHoursPerDay;

    private LocalDate interviewDate;

    private String targetCompanies;

    private String techStack;


}
