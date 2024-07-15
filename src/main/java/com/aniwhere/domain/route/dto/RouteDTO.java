package com.aniwhere.domain.route.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class RouteDTO {
    private Long id;
    private String userId;
    private String name;
    private String description;
    private LocalDateTime createdAt;
    private String userName;
    private String image;
    private List<MarkerDTO> markers;
}
