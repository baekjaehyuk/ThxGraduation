package com.thxgraduate.common.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI springOpenApi() {
        return new OpenAPI()
                .info(new Info()
                        .title("Thx-Graduate API Documentation")
                        .description("졸업을 축하해 서비스의 API 명세서입니다.")
                        .version("v1.0.0"))
                .components(securityComponents())
                .addSecurityItem(new io.swagger.v3.oas.models.security.SecurityRequirement().addList("AccessToken"));
    }

    private Components securityComponents() {
        return new Components()
                .addSecuritySchemes(
                        "AccessToken",
                        new SecurityScheme()
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")
                );
    }
}
