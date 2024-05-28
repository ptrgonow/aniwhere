package com.aniwhere.domain.route.domain;

import lombok.Data;

@Data
public class Marker {

    private Long makerId;
    private Long routeId;
    private Double longitude;
    private Double latitude;
}
