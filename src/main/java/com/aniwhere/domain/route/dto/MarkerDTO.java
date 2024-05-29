package com.aniwhere.domain.route.dto;

import lombok.Data;

@Data
public class MarkerDTO {

    private Long makerId;
    private Long routeId;
    private Double longitude;
    private Double latitude;

}
