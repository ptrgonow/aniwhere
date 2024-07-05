package com.aniwhere.domain.user.loginSession.dto;

import lombok.Data;

@Data
public class LoginDTO {

    private String userId;
    private String userPwd;
    private String userName;
    private String role;
}
