package com.aniwhere.domain.usermng.loginSession.dto;

import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.Collections;

@Data
public class LoginDTO {

    private String userName;
    private String password;
    private String role;
}
