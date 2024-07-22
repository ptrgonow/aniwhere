package com.aniwhere.domain.admin.dto;

import lombok.Data;

@Data
public class AdminListDetailDTO {
    private String userId;
    private String userName;
    private String email;
    private String address;
    private String detailAddress;
    private String zipCode;
    private String phone;
    private String createdAt;
    private String role; // 역할 필드 추가
}

