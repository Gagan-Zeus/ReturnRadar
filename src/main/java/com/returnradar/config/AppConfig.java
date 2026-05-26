package com.returnradar.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.json.JsonMapper;

@Configuration
public class AppConfig {

    @Bean
    public ObjectMapper objectMapper() {
        // Jackson 3 has Java time support built in through its JavaTimeInitializer.
        return JsonMapper.builder().build();
    }
}
