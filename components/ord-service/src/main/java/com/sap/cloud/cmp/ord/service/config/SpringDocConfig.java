package com.sap.cloud.cmp.ord.service.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringDocConfig {

    @Value("${static.api.version}")
    private String apiVersion;

    @Value("${springdoc.openapi-version}")
    private String openAPIVersion;

    @Bean
    public OpenAPI ordServiceAPI() {
        return new OpenAPI()
                .info(new Info().title("ORD Service")
                        .description("REST API for ORD Service")
                        .version(apiVersion)).openapi(openAPIVersion);
    }
}