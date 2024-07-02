package com.aniwhere.global.config.util;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@Getter
public class Token {

    @Value("${mapbox.access.token}")
    private String mapboxAccessToken;

    @GetMapping("/getMapboxAccessToken")
    public Map<String, String> getMapboxAccessToken() {
        Map<String, String> response = new HashMap<>();
        response.put("token", mapboxAccessToken);
        return response;
    }
}
