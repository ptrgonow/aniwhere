package com.aniwhere.domain.usermng.loginSession.dto;

import lombok.Data;

@Data
public class LoginDTO {

    private String userName;
    private String password;
    private String role;
}
