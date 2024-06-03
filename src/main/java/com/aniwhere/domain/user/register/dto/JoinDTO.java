package com.aniwhere.domain.user.register.dto;

import lombok.Data;

@Data
public class JoinDTO {

    private String userName;
    private String password;
    private String email;
    private String address;
    private String role;
}
