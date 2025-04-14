package com.t9radar.radar.config;

import io.swagger.v3.oas.models.OpenAPI;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@Configuration
public class OpenApiConfiguration implements WebMvcConfigurer {
  @Bean
  public OpenAPI openApi() {
    return new OpenAPI();
  }

  /* TODO:
  @Bean
  public OpenApiCustomiser sortSchemasAlphabetically() {
    return openApi -> {
      Map<String, Schema> schemas = openApi.getComponents().getSchemas();
      openApi.getComponents().setSchemas(new TreeMap<>(schemas));
    };
  }
  */
}
