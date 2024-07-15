package com.aniwhere.domain.user.join.domain;

import lombok.Data;

@Data
public class Join {

    private String userId;
    private String userPwd;
    private String userName;
    private String email;
    private String address;
    private String detailAddress;
    private String zipCode;
    private String phone;
    private String role;

}
