package com.t9radar.radar.config;

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
        "classpath:/messages/radar",
        "classpath:/messages/ring",
        "classpath:/messages/segment",
        "classpath:/messages/technology",
        "classpath:/messages/technology_blip",
        "classpath:/messages/tenant"
    );
    messageSource.setDefaultEncoding("UTF-8");
    return messageSource;
  }
}
