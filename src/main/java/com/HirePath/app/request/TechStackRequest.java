package com.HirePath.app.request;

import com.HirePath.app.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TechStackRequest {

    private List<String> techStacks;

    private User user;

}
