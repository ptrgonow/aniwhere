package com.aniwhere.domain.user.join.dto;

import lombok.Data;

@Data
public class MailCodeCheckDTO {
    private String email;
    private String authCode;
}
