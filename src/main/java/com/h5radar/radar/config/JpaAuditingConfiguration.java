package com.h5radar.radar.config;

import java.util.Optional;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Configuration
@EnableJpaAuditing(auditorAwareRef = "auditorProvider")
public class JpaAuditingConfiguration {

  @Bean
  public AuditorAware<String> auditorProvider() {
    //TODO Replace with Spring Security
    return () -> Optional.of("h5radar");
  }

}
