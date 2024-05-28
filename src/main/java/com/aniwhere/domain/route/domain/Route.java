package com.aniwhere.domain.route.domain;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Route {

    private Long routeId;
    private String routeName;
    private String routeCont;
    private LocalDateTime createAt;
    private LocalDateTime updateAt;

}
