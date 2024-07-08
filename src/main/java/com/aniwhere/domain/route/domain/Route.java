package com.aniwhere.domain.route.domain;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Route {
    private Long id;
    private String userId;
    private String name;
    private String description;
    private LocalDateTime createdAt;
}
