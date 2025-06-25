package com.h5radar.radar.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.h5radar.radar.config.AuthRequestInterceptor;

@Configuration
public class WebConfig implements WebMvcConfigurer {

  @Autowired
  private AuthRequestInterceptor authRequestInterceptor;

  @Override
  public void addInterceptors(InterceptorRegistry registry) {
    registry.addInterceptor(authRequestInterceptor)
        .addPathPatterns("/api/v1/radar-app/**")
        .excludePathPatterns("/actuator/**")
        .excludePathPatterns("/swagger-ui/**")
        .excludePathPatterns("/v3/api-docs/**");
  }
}
