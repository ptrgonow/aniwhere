package com.aniwhere.domain.usermng.loginSession.domain;

import lombok.Data;

@Data
public class LoginSession {

    private String username;
    private String password;
    private String role;

}
