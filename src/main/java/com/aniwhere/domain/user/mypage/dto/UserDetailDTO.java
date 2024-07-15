package com.aniwhere.domain.user.mypage.dto;

import lombok.Data;

@Data
public class UserDetailDTO {

    private String userId;
    private String userPwd;
    private String userName;
    private String email;
    private String address;
    private String detailAddress;
    private String zipCode;
    private String phone;
    private String role;
    private String createdAt;
    private boolean isSocial;

}
