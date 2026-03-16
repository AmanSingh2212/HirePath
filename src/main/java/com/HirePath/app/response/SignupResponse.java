package com.HirePath.app.response;

import com.HirePath.app.datatypes.User_Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SignupResponse {

    private String jwt;

    private String message;

    private User_Role role;

}
