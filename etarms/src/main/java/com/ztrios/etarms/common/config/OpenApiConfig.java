package com.ztrios.etarms.common.config;

import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.OpenAPI;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI etarmsOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("ETARMS API")
                        .description("Employee Task, Attendance & Reporting Management System APIs")
                        .version("1.0.0"));
    }
}
