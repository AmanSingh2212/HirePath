package com.HirePath.app.entity;

import com.HirePath.app.datatypes.User_Role;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    @Column(unique = true)
    private String email;

    private String password;

    private User_Role role;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @OneToOne
    private UserProfile userProfile;

    @OneToOne(fetch = FetchType.EAGER) // ✅ EAGER so TechStack is always loaded with User
    private TechStack techStacks;

    @JsonIgnore
    @ManyToMany
    @JoinTable(
            name = "user_question_lists",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "question_list_id")
    )
    private List<QuestionList> questionLists;

    private int dailyApiCallCount = 0;

    private LocalDate lastApiCallDate;

}
