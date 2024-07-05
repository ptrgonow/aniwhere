package com.aniwhere;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class AniwhereApplication {

    public static void main(String[] args) {
        SpringApplication.run(AniwhereApplication.class, args);
    }

}
