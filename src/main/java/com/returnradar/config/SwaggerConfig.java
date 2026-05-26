package com.returnradar.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI returnRadarOpenApi() {
        return new OpenAPI()
                .info(new Info()
                        .title("ReturnRadar API")
                        .description("Reverse Logistics Analytics System for E-Commerce")
                        .version("1.0.0")
                        .contact(new Contact().name("Team ReturnRadar")));
    }
}
