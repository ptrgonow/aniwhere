package com.aniwhere.domain.user.mypage.domain;

import lombok.Data;

@Data
public class UserDetail {

    private int Id;
    private String userId;
    private String userPwd;
    private String userName;
    private String email;
    private String address;
    private String detailAddress;
    private String zipCode;
    private String phone;
}
