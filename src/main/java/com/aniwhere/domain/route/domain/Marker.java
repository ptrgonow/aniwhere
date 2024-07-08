package com.aniwhere.domain.route.domain;

import lombok.Data;

@Data
public class Marker {
    private Long id;
    private Long routeId;
    private Double longitude;
    private Double latitude;
}
