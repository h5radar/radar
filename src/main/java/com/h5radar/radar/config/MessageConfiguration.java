package com.h5radar.radar.config;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MessageConfiguration implements WebMvcConfigurer {

  @Bean
  public MessageSource messageSource() {
    ReloadableResourceBundleMessageSource messageSource =
        new ReloadableResourceBundleMessageSource();
    messageSource.setBasenames(
        "classpath:/messages/application",
        "classpath:/messages/domain",
        "classpath:/messages/license",
        "classpath:/messages/maturity",
        "classpath:/messages/technology"
    );
    messageSource.setDefaultEncoding("UTF-8");
    return messageSource;
  }
}
