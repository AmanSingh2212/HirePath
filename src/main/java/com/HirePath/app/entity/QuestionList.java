package com.HirePath.app.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class QuestionList {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToMany(cascade = CascadeType.ALL)
    private List<DsaQuestion> selected_questions;

    @OneToMany(cascade = CascadeType.ALL)
    private List<SystemDesignQuestion> system_design_questions;

    @OneToMany(cascade = CascadeType.ALL)
    private List<DevProblem> development_problems;

    @JsonIgnore
    @ManyToMany(mappedBy = "questionLists")
    private List<User> user;
}
