package com.aniwhere.global.config.util;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;

@Component("token")
@Getter
public class Token {

    @Value("${mapbox.access.token}")
    private String mapboxAccessToken;

    @GetMapping("/getMapboxAccessToken")
    public String getMapboxAccessToken() {
        return mapboxAccessToken;
    }

}
