package com.example.simplecrm.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI simpleCrmOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Simple Crm API")
                        .description("Simple Customer Management System built with Spring Boot")
                        .version("1.0.0")
                );
    }
}
