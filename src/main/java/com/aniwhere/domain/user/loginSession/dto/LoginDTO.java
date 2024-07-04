package com.aniwhere.domain.user.loginSession.dto;

import lombok.Data;

@Data
public class LoginDTO {

    private String userId;
    private String password;
    private String role;
}
