package com.aniwhere.domain.route.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class RouteDTO {

    private Long routeId;
    private String routeName;
    private String routeCont;
    private LocalDateTime createAt;
    private LocalDateTime updateAt;
    private List<MarkerDTO> markers;

}
