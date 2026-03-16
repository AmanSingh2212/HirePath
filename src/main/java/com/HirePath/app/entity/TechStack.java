package com.HirePath.app.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;


@Entity
@Table(name = "tech_stacks")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TechStack {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @JsonIgnore
    @OneToOne
    private User user;

    @ElementCollection(fetch = FetchType.EAGER) // ✅ Required to persist List<String>
    @CollectionTable(name = "tech_stack_names", joinColumns = @JoinColumn(name = "tech_stack_id"))
    @Column(name = "name")
    private List<String> names;
}
