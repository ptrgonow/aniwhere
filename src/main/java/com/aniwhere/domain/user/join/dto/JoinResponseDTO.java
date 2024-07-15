package com.aniwhere.domain.user.join.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class JoinResponseDTO {
    private boolean success;
    private String message;
}
