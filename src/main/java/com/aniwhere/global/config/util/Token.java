package com.aniwhere.global.config.util;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component("token")
@Getter
public class Token {

    @Value("${mapbox.access.token}")
    private String mapboxAccessToken;

}
