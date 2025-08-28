package com.h5radar.radar.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@Configuration
public class OpenApiConfiguration implements WebMvcConfigurer {

  private SecurityScheme createApiKeyScheme() {
    return new SecurityScheme().type(SecurityScheme.Type.HTTP)
        .bearerFormat("JWT")
        .scheme("bearer");
  }

  @Bean
  public OpenAPI openApi() {
    return new OpenAPI().addSecurityItem(new SecurityRequirement()
            .addList("Bearer Authentication"))
        .components(new Components()
            .addSecuritySchemes("Bearer Authentication", createApiKeyScheme()));
  }
}
