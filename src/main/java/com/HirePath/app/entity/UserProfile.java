package com.HirePath.app.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "user_profiles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private int experienceYears;

    private int availableHoursPerDay;

    private LocalDate interviewDate;

    private String targetCompanies; // comma separated

    private String techStack; // Java, Spring Boot, React
}