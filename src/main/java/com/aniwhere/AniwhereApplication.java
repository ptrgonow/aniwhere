package com.aniwhere;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
public class AniwhereApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(AniwhereApplication.class, args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(AniwhereApplication.class);
    }

    @Bean
    public CommandLineRunner printActiveProfiles(Environment env) {
        return args -> {
            System.out.println("Active profiles: " + String.join(", ", env.getActiveProfiles()));
        };
    }
}
