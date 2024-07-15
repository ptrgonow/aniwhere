package com.aniwhere.domain.user.mypage.dto;

import lombok.Data;

@Data
public class UpdateDetailDTO {

    private int id;
    private String userId;
    private String userPwd;
    private String newUserPwd;
    private String userName;
    private String email;
    private String address;
    private String detailAddress;
    private String zipCode;
    private String phone;

}
