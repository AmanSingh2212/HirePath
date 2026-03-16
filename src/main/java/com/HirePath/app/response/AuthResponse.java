package com.HirePath.app.response;

import com.HirePath.app.datatypes.User_Role;
import com.HirePath.app.entity.RefreshToken;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponse {

//      private String jwt;

    private RefreshToken refreshToken;

    private String jwt;

    private String message;

    private User_Role role;

}
